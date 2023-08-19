import React from 'react';
import '@patternfly/react-core/dist/styles/base.css';
import {
  Card, CardBody,
  Page, PageSection,
  Alert, FormAlert,
  FormGroup, TextInput,
  Stack, StackItem, FormHelperText, HelperText, HelperTextItem, Label, Spinner, SimpleListItem, SimpleList, ToggleGroup, ToggleGroupItem, List, ListItem
} from '@patternfly/react-core';
import { Table, Thead, Tr, Th, Tbody, Td } from '@patternfly/react-table';
import { configureStore, createAction } from '@reduxjs/toolkit';
import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { useDispatch, useSelector } from 'react-redux';

import { ValidatorEndpointApi, SuggestCompletionEndpointApi, ExecutorEndpointApi, SyntaxErrorDTO, ExecutionResultDTO, ExecutionDTO } from './generated';
import { firstValueFrom } from 'rxjs';
import { AjaxError } from 'rxjs/ajax';
import { Form, Field } from 'react-final-form';

const validator = new ValidatorEndpointApi();
const suggest = new SuggestCompletionEndpointApi();
const executor = new ExecutorEndpointApi();

const useAppDispatch: () => AppDispatch = useDispatch

interface Sample {
  salesPerson: string,
  region: string,
  salesAmount: number,
  percentCommission: number,
  commissionAmount: string | null,
  status: 'notExecutedYet' | 'executed' | 'processing' | 'failed' | 'formulaInError' | 'formulaInvalid',
  executions: Array<ExecutionDTO>
};

interface FormulaState {
  formula: string;
  invalidMessage: string | null;
  errorMessage: string | null;
  status: 'notValidated' | 'validationProcessing' | 'valid' | 'invalid' | 'error'
};

const initialFormulaState: FormulaState = {
  formula: '',
  invalidMessage: null,
  errorMessage: null,
  status: 'notValidated'
};

interface AutoSuggestionState {
  tokens: string[];
  errMessage: string | null;
  status: 'idle' | 'loading' | 'failed';
};

const initialAutoSuggestionState: AutoSuggestionState = {
  tokens: [],
  errMessage: null,
  status: 'idle'
};

interface SamplesState {
  samples: Sample[];
};

const initialSamplesState: SamplesState = {
  samples: [
    { salesPerson: 'Joe', region: 'North', salesAmount: 260, percentCommission: 10, commissionAmount: null, status: 'notExecutedYet', executions: [] },
    { salesPerson: 'Robert', region: 'South', salesAmount: 660, percentCommission: 15, commissionAmount: null, status: 'notExecutedYet', executions: [] },
    { salesPerson: 'Michelle', region: 'East', salesAmount: 940, percentCommission: 15, commissionAmount: null, status: 'notExecutedYet', executions: [] },
    { salesPerson: 'Erich', region: 'West', salesAmount: 410, percentCommission: 12, commissionAmount: null, status: 'notExecutedYet', executions: [] },
    { salesPerson: 'Dafna', region: 'North', salesAmount: 800, percentCommission: 15, commissionAmount: null, status: 'notExecutedYet', executions: [] },
    { salesPerson: 'Rob', region: 'South', salesAmount: 900, percentCommission: 15, commissionAmount: null, status: 'notExecutedYet', executions: [] }
  ]
};

const selectFormulaPreset = createAction<string>('preset/select');

const defineFormula = createAction<string>('formula/define');

interface RemoteError {
  message: string
}

const validateFormula = createAsyncThunk<
  SyntaxErrorDTO | null, void,
  {
    rejectValue: RemoteError
  }>('formula/validate', async (_void: void, { rejectWithValue }) => {
    try {
      const formula: string = store.getState().formula.formula;
      const syntaxError: SyntaxErrorDTO | null = await firstValueFrom(validator.validate({ formula: formula }));
      return syntaxError;
    } catch (error) {
      if (error instanceof AjaxError) {
        if ('application/vnd.validation-unexpected-exception-v1+json' === error.xhr.getResponseHeader('content-type')) {
          throw rejectWithValue({
            message: error.response.message
          });
        } else {
          throw error;
        }
      } else {
        throw error;
      }
    }
  });

const formulaSlice = createSlice({
  name: 'formula',
  initialState: initialFormulaState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(selectFormulaPreset, (state, { payload }) => {
        state.formula = payload;
        state.status = 'notValidated';
        state.invalidMessage = null;
        state.errorMessage = null;
      })
      .addCase(defineFormula, (state, { payload }) => {
        state.formula = payload;
        state.status = 'notValidated';
        state.invalidMessage = null;
        state.errorMessage = null;
      })
      .addCase(validateFormula.rejected, (state, { payload }) => {
        state.status = 'error';
        state.invalidMessage = null;
        state.errorMessage = payload!.message;
      })
      .addCase(validateFormula.pending, (state) => {
        state.status = 'validationProcessing';
        state.invalidMessage = null;
        state.errorMessage = null;
      })
      .addCase(validateFormula.fulfilled, (state, { payload }) => {
        if (payload === null) {
          state.status = 'valid';
          state.invalidMessage = null;
          state.errorMessage = null;
        } else {
          state.status = 'invalid';
          state.invalidMessage = payload.msg!;
          state.errorMessage = null;
        }
      });
  },
});

const suggestTokens = createAsyncThunk<
  string[], void,
  {
    rejectValue: RemoteError
  }>('autoSuggestion/suggestTokens', async (_void: void, { rejectWithValue }) => {
    try {
      const formula: string = store.getState().formula.formula;
      const tokens: string[] = await firstValueFrom(suggest.suggestCompletion({
        suggestedFormula: formula
      }));
      return tokens;
    } catch (error) {
      if (error instanceof AjaxError) {
        switch (error.xhr.getResponseHeader('content-type')) {
          case 'application/vnd.autosuggestion-unavailable-v1+json':
            throw rejectWithValue({
              message: error.response.message
            });
          case 'application/vnd.autosuggestion-execution-exception-v1+json':
            throw rejectWithValue({
              message: error.response.message
            });
          case 'application/vnd.autosuggestion-unexpected-exception-v1+json':
            throw rejectWithValue({
              message: error.response.message
            });
          case 'application/vnd.autosuggestion-execution-timed-out-v1+json':
            throw rejectWithValue({
              message: error.response.message
            });
          default:
            throw error;
        }
      } else {
        throw error;
      }
    }
  });

const autoSuggestionSlice = createSlice({
  name: 'autoSuggestion',
  initialState: initialAutoSuggestionState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(suggestTokens.rejected, (state, { payload }) => {
        state.status = 'failed';
        state.errMessage = payload!.message;
        state.tokens = [];
      })
      .addCase(suggestTokens.pending, (state) => {
        state.status = 'loading';
        state.errMessage = null;
        state.tokens = [];
      })
      .addCase(suggestTokens.fulfilled, (state, { payload }) => {
        state.status = 'idle';
        state.errMessage = null;
        state.tokens = payload;
      });
  },
});

const executeFormulaOnSamples = createAsyncThunk<Sample[], void
>('samples/executeFormula', async (_void: void) => {
  const formula: string = store.getState().formula.formula;
  const samples: Sample[] = store.getState().samples.samples;
  let status: 'notExecutedYet' | 'executed' | 'processing' | 'failed' | 'formulaInError' | 'formulaInvalid' = 'notExecutedYet';
  let commissionAmount: string | null = null;
  let executions: Array<ExecutionDTO> = [];
  const results: Sample[] = await Promise.all(samples.map(async function (sample: Sample) {
    try {
      const executionResult: ExecutionResultDTO = await firstValueFrom(executor.execute({
        executeDTO: {
          formula: formula,
          structuredData: {
            'Sales Person': sample.salesPerson,
            'Region': sample.region,
            'Sales Amount': sample.salesAmount.toString(),
            '% Commission': sample.percentCommission.toString()
          }
        }
      }));
      status = 'executed';
      commissionAmount = executionResult.result!;
      executions = executionResult.executions!;
    } catch (error) {
      status = 'failed';
      if (error instanceof AjaxError) {
        switch (error.xhr.getResponseHeader('content-type')) {
          case 'application/vnd.execution-syntax-error-v1+json':
            // TODO
            break;
          case 'application/vnd.execution-unexpected-exception-v1+json':
            // TODO
            break;
          default:
            // TODO
            break;
        }
      } else {
        // TODO
      }
    }
    return {
      salesPerson: sample.salesPerson,
      region: sample.region,
      salesAmount: sample.salesAmount,
      percentCommission: sample.percentCommission,
      commissionAmount,
      status,
      executions
    };
  }));
  return results;
});

const markSamplesAsFormulaInError = createAction('samples/markAsFormulaInError');

const markSamplesAsFormulaInvalid = createAction('samples/markAsFormulaInvalid');

const samplesSlice = createSlice({
  name: 'samples',
  initialState: initialSamplesState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(executeFormulaOnSamples.rejected, (state, { payload }) => {
        // TODO
      })
      .addCase(executeFormulaOnSamples.pending, (state) => {
        /*
         * Avoid flickering because it is really fast
        state.samples.forEach(sample => {
          sample.status = 'processing';
        })
        */
      })
      .addCase(executeFormulaOnSamples.fulfilled, (state, { payload }) => {
        state.samples = payload;
      })
      .addCase(markSamplesAsFormulaInError, (state) => {
        state.samples.forEach(sample => {
          sample.status = 'formulaInError';
        });
      })
      .addCase(markSamplesAsFormulaInvalid, (state) => {
        state.samples.forEach(sample => {
          sample.status = 'formulaInvalid';
        });
      })
  }
})

const selectSamples = (state: RootState) => state.samples;
const selectFormula = (state: RootState) => state.formula;
const selectTokens = (state: RootState) => state.autoSuggestion.tokens;
const selectAutoSuggestionStatus = (state: RootState) => state.autoSuggestion.status;
const selectAutoSuggestionErrMessage = (state: RootState) => state.autoSuggestion.errMessage;
const selectExecutionsDebug = (state: RootState) => state.executionsDebug;

interface Executions {
  salesPerson: string,
  executions: Array<ExecutionDTO>
}

interface ExecutionsDebugState {
  selectedSalesPerson: string | null,
  executions: Array<Executions>
}

const initialExecutionsDebugState: ExecutionsDebugState = {
  selectedSalesPerson: null,
  executions: []
}

const selectSalesPerson = createAction<string>('executionsDebug/selectSalesPerson');

const executionsDebugSlice = createSlice({
  name: 'executionsDebug',
  initialState: initialExecutionsDebugState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(selectSalesPerson, (state, { payload }) => {
        state.selectedSalesPerson = payload;
      })
      .addCase(executeFormulaOnSamples.rejected, (state, { payload }) => {
        state.executions = []
      })
      .addCase(executeFormulaOnSamples.pending, (state) => {
        state.executions = []
      })
      .addCase(executeFormulaOnSamples.fulfilled, (state, { payload }) => {
        state.executions = payload.map(sample => {
          return {
            salesPerson: sample.salesPerson,
            executions: sample.executions
          }
        })
      })
      .addCase(markSamplesAsFormulaInError, (state) => {
        state.executions = []
      })
      .addCase(markSamplesAsFormulaInvalid, (state) => {
        state.executions = []
      })
  }
})

export const store = configureStore({
  reducer: {
    formula: formulaSlice.reducer,
    autoSuggestion: autoSuggestionSlice.reducer,
    samples: samplesSlice.reducer,
    executionsDebug: executionsDebugSlice.reducer
  },
});

type RootState = ReturnType<typeof store.getState>;
type AppDispatch = typeof store.dispatch;

const Formula: React.FunctionComponent<{}> = () => {
  const dispatch = useAppDispatch();
  const formula = useSelector(selectFormula);
  const tokens = useSelector(selectTokens);
  const autoSuggestionStatus = useSelector(selectAutoSuggestionStatus);
  const autoSuggestionErrMessage = useSelector(selectAutoSuggestionErrMessage);

  function onFormulaPresetSelected(preset: string) {
    dispatch(selectFormulaPreset(preset));
    executeFormula();
  }

  function onFormulaDefinition(formulaKeyUpEvent: React.KeyboardEvent<HTMLInputElement>): void {
    const formula: string = formulaKeyUpEvent.currentTarget.value;
    dispatch(defineFormula(formula));
    if (formulaKeyUpEvent.key === 'Enter') {
      executeFormula();
    } else {
      dispatch(suggestTokens());
    }
  }

  function executeFormula() {
    dispatch(validateFormula())
      .then((resultAction) => {
        if (validateFormula.fulfilled.match(resultAction)) {
          // cannot use selector here
          switch (store.getState().formula.status) {
            case 'valid':
              dispatch(executeFormulaOnSamples());
              break;
            case 'invalid':
              dispatch(markSamplesAsFormulaInvalid());
              break;
            default:
              console.log('Should not be here');
          }
        } else {
          dispatch(markSamplesAsFormulaInError());
        }
      });
  }

  return (
    <FormGroup>
      <Form
        initialValues={{
          formulaDefinition: ''
        }}
        mutators={{
          setInputFormulaDefinition: (formulaDefinition: string, state, utils) => {
            utils.changeValue(state, 'formulaDefinition', () => formulaDefinition)
          }
        }}
        onSubmit={() => { }}
        render={({ form }) => (
          <div>
            <SimpleList>
              <SimpleListItem key="firstPresetFormula" onClick={() => {
                onFormulaPresetSelected("MUL([@[Sales Amount]],[@[% Commission]])");
                form.mutators.setInputFormulaDefinition(store.getState().formula.formula);
              }}>
                Compute commission amount by multiplying Sales Amount by Percent Commission
              </SimpleListItem>
              <SimpleListItem key="secondPresetFormula" onClick={() => {
                onFormulaPresetSelected("IF(EQ([@[Sales Person]],\"Joe\"),MUL(MUL([@[Sales Amount]],[@[% Commission]]),2),MUL([@[Sales Amount]],[@[% Commission]]))");
                form.mutators.setInputFormulaDefinition(store.getState().formula.formula);
              }}>
                Compute commission amount by multiplying Sales Amount by Percent Commission if it is Joe mulitply by two
              </SimpleListItem>
            </SimpleList>
            <Field name="formulaDefinition">
              {props => (
                <TextInput id="formulaDefinition"
                  onChange={props.input.onChange}
                  value={props.input.value}
                  onKeyUp={formulaKeyUpEvent => onFormulaDefinition(formulaKeyUpEvent)}
                />
              )}
            </Field>
          </div>
        )} />
      <FormHelperText>
        <HelperText>
          {autoSuggestionStatus === 'idle' &&
            <HelperTextItem variant={'success'}>{tokens.join(',')}</HelperTextItem>
          }
          {autoSuggestionStatus === 'loading' &&
            <HelperTextItem variant={'indeterminate'}>Loading...</HelperTextItem>
          }
          {autoSuggestionStatus === 'failed' &&
            <HelperTextItem variant={'error'}>{autoSuggestionErrMessage}</HelperTextItem>
          }
        </HelperText>
      </FormHelperText>
      <FormAlert>
        {formula.status === 'error' &&
          <Alert variant="danger" title={formula.errorMessage} aria-live="polite" isInline />
        }
        {formula.status === 'valid' &&
          <Alert variant="success" title="Formula is valid." aria-live="polite" isInline />
        }
        {formula.status === 'validationProcessing' &&
          <Alert variant="info" title="Validation in progress." aria-live="polite" isInline />
        }
        {formula.status === 'invalid' &&
          <Alert variant="danger" title={formula.invalidMessage} aria-live="polite" isInline />
        }
      </FormAlert>
    </FormGroup>
  )
}

const Samples: React.FunctionComponent<{}> = () => {
  const samples = useSelector(selectSamples);
  const columnNames = {
    salesPerson: 'Sales Person',
    region: 'Region',
    salesAmount: 'Sales Amount',
    percentCommission: '% Commission',
    commissionAmount: 'Commission Amount'
  };

  return (
    <Table>
      <Thead>
        <Tr>
          <Th width={15}>{columnNames.salesPerson}</Th>
          <Th width={15}>{columnNames.region}</Th>
          <Th width={15}>{columnNames.salesAmount}</Th>
          <Th width={15}>{columnNames.percentCommission}</Th>
          <Th width={15}>{columnNames.commissionAmount}</Th>
          <Td width={25}></Td>
        </Tr>
      </Thead>
      <Tbody>
        {samples.samples.map(sample => (
          <Tr key={sample.salesPerson}>
            <Td dataLabel={columnNames.salesPerson}>{sample.salesPerson}</Td>
            <Td dataLabel={columnNames.region}>{sample.region}</Td>
            <Td dataLabel={columnNames.salesAmount}>{sample.salesAmount}</Td>
            <Td dataLabel={columnNames.percentCommission}>{sample.percentCommission}</Td>
            <Td dataLabel={columnNames.commissionAmount}>
              {sample.status === 'notExecutedYet' &&
                <Label color="blue">Not executed yet</Label>
              }
              {sample.status === 'executed' &&
                <Label color="green">{sample.commissionAmount}</Label>
              }
              {sample.status === 'processing' &&
                <Spinner size="sm" />
              }
              {sample.status === 'failed' &&
                <Label color="red">Somethings wrong happened</Label>
              }
              {sample.status === 'formulaInError' &&
                <Label color="red">Formula in error unable to process</Label>
              }
              {sample.status === 'formulaInvalid' &&
                <Label color="orange">Formula invalid</Label>
              }
            </Td>
          </Tr>
        ))}
      </Tbody>
    </Table>
  )
}

const ExecutionDebug: React.FunctionComponent<{}> = () => {
  const formula = useSelector(selectFormula);
  const samples = useSelector(selectSamples);
  const executionsDebug = useSelector(selectExecutionsDebug);
  const dispatch = useAppDispatch();
  const columnNames = {
    executionId: 'Execution Id',
    executedAt: 'Executed At',
    inputs: 'Inputs',
    result: 'Result',
    underline: 'Underline'
  };

  return (
    <React.Fragment>
      <ToggleGroup>
        {samples.samples.map(sample => {
          return (
            <ToggleGroupItem
              key={sample.salesPerson}
              text={sample.salesPerson}
              isSelected={sample.salesPerson === executionsDebug.selectedSalesPerson}
              onChange={() => dispatch(selectSalesPerson(sample.salesPerson))}
            />)
        })}
      </ToggleGroup>
      <Table>
        <Thead>
          <Tr>
            <Td>{columnNames.executionId}</Td>
            <Td>{columnNames.executedAt}</Td>
            <Td>{columnNames.inputs}</Td>
            <Td>{columnNames.result}</Td>
            <Td>{columnNames.underline}</Td>
          </Tr>
        </Thead>
        <Tbody>
          {executionsDebug.executions.filter(executions => executions.salesPerson === executionsDebug.selectedSalesPerson)
            .flatMap(executions => executions.executions)
            .map((execution: ExecutionDTO, index: number) => {
              return (
                <Tr key={index}>
                  <Td dataLabel={columnNames.executionId}>{execution.executionId}</Td>
                  <Td dataLabel={columnNames.executedAt}>{execution.executedAt}</Td>
                  <Td dataLabel={columnNames.inputs}>
                    <List isPlain isBordered>
                      {
                        Object.entries(execution.inputs!)
                          .map(([key, value]) => {
                            return (
                              <ListItem key={key}>{key}: {value}</ListItem>
                            )
                          })
                      }
                    </List>
                  </Td>
                  <Td dataLabel={columnNames.result}>{execution.result}</Td>
                  <Td dataLabel={columnNames.underline}>
                    {
                      formula.formula.substring(0, execution.start!)
                    }
                    <b>
                      {
                        formula.formula.substring(execution.start!, execution.end! + 1)
                      }
                    </b>
                    {
                      formula.formula.substring(execution.end! + 1, formula.formula.length)
                    }
                  </Td>
                </Tr>
              )
            })}
        </Tbody>
      </Table>
    </React.Fragment>
  );
}

function App() {
  return (
    <Page>
      <PageSection isWidthLimited isCenterAligned>
        <Card>
          <CardBody>
            <Stack hasGutter>
              <StackItem>
                <Formula />
              </StackItem>
              <StackItem isFilled>
                <Samples />
              </StackItem>
              <StackItem>
                <ExecutionDebug />
              </StackItem>
            </Stack>
          </CardBody>
        </Card>
      </PageSection>
    </Page>
  );
};

export default App;
