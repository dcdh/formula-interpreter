import React from 'react';
import '@patternfly/react-core/dist/styles/base.css';
import {
  Card, CardBody,
  Page, PageSection,
  Alert,
  FormGroup, TextInput,
  Label, Spinner, ToggleGroup, ToggleGroupItem, List, ListItem, Popper, Menu, MenuContent,
  MenuList, MenuItem, InputGroup, InputGroupItem, DropdownList, DropdownItem, Dropdown, MenuToggle, MenuToggleElement,
  Grid, GridItem, CardHeader, ValidatedOptions, AlertProps, AlertGroup, AlertVariant, AlertActionCloseButton, Stack, StackItem, FlexItem, Flex
} from '@patternfly/react-core';
import { Table, Thead, Tr, Th, Tbody, Td } from '@patternfly/react-table';
import { configureStore, createAction } from '@reduxjs/toolkit';
import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { useDispatch, useSelector } from 'react-redux';

import { ValidatorEndpointApi, SuggestCompletionEndpointApi, ExecutorEndpointApi, SyntaxErrorDTO, ExecutionResultDTO, ExecutionDTO } from './generated';
import { firstValueFrom } from 'rxjs';
import { AjaxError } from 'rxjs/ajax';
import { Form, Field } from 'react-final-form';
import { CheckCircleIcon, OutlinedClockIcon } from '@patternfly/react-icons';

const validator = new ValidatorEndpointApi();
const suggest = new SuggestCompletionEndpointApi();
const executor = new ExecutorEndpointApi();

const useAppDispatch: () => AppDispatch = useDispatch

interface SampleState {
  salesPerson: string,
  region: string,
  salesAmount: number,
  percentCommission: number,
  commissionAmount: string | null,
  status: 'notExecutedYet' | 'executed' | 'processing' | 'failed' | 'formulaInError' | 'formulaInvalid',
  executions: ExecutionsResultState | null
};

interface ExecutionsResultState {
  result: string,
  processedInNanos: number,
  executions: Array<ExecutionState>
};

interface ExecutionState {
  executedAtStart: string;
  executedAtEnd: string;
  position?: PositionState;
  inputs: { [key: string]: string; };
  result: string;
};

interface PositionState {
  start: number;
  end: number;
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
  samples: SampleState[];
};

const initialSamplesState: SamplesState = {
  samples: [
    { salesPerson: 'Joe', region: 'North', salesAmount: 260, percentCommission: 10, commissionAmount: null, status: 'notExecutedYet', executions: null },
    { salesPerson: 'Robert', region: 'South', salesAmount: 660, percentCommission: 15, commissionAmount: null, status: 'notExecutedYet', executions: null },
    { salesPerson: 'Michelle', region: 'East', salesAmount: 940, percentCommission: 15, commissionAmount: null, status: 'notExecutedYet', executions: null },
    { salesPerson: 'Erich', region: 'West', salesAmount: 410, percentCommission: 12, commissionAmount: null, status: 'notExecutedYet', executions: null },
    { salesPerson: 'Dafna', region: 'North', salesAmount: 800, percentCommission: 15, commissionAmount: null, status: 'notExecutedYet', executions: null },
    { salesPerson: 'Rob', region: 'South', salesAmount: 900, percentCommission: 15, commissionAmount: null, status: 'notExecutedYet', executions: null }
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

const executeFormulaOnSamples = createAsyncThunk<SampleState[], void
>('samples/executeFormula', async (_void: void) => {
  const formula: string = store.getState().formula.formula;
  const samples: SampleState[] = store.getState().samples.samples;
  let status: 'notExecutedYet' | 'executed' | 'processing' | 'failed' | 'formulaInError' | 'formulaInvalid' = 'notExecutedYet';
  let commissionAmount: string | null = null;
  let executions: ExecutionsResultState | null = null;
  const results: SampleState[] = await Promise.all(samples.map(async function (sample: SampleState) {
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
      executions = {
        result: executionResult.result!,
        processedInNanos: executionResult.processedInNanos!,
        executions: executionResult.executions!.map((execution: ExecutionDTO) => {
          return {
            executedAtStart: execution.executedAtStart!,
            executedAtEnd: execution.executedAtEnd!,
            position: {
              start: execution.position!.start!,
              end: execution.position!.end!
            },
            inputs: execution.inputs!,
            result: execution.result!
          }
        })
      };
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
      .addCase(executeFormulaOnSamples.rejected, () => {
        // TODO
      })
      .addCase(executeFormulaOnSamples.pending, () => {
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
const selectExecutionsDebug = (state: RootState) => state.executionsDebug;

interface ExecutionsState {
  salesPerson: string,
  executionsResult: ExecutionsResultState
}

interface ExecutionsDebugState {
  selectedSalesPerson: string | null,
  executions: Array<ExecutionsState>
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
      .addCase(executeFormulaOnSamples.rejected, (state) => {
        state.executions = []
      })
      .addCase(executeFormulaOnSamples.pending, (state) => {
        state.executions = []
      })
      .addCase(executeFormulaOnSamples.fulfilled, (state, { payload }) => {
        state.executions = payload.map((sample: SampleState) => {
          return {
            salesPerson: sample.salesPerson,
            executionsResult: sample.executions!
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

const Formula = (props: { addAlert: (title: string, variant: AlertProps['variant']) => void; }) => {
  const dispatch = useAppDispatch();
  const formula = useSelector(selectFormula);
  const tokens = useSelector(selectTokens);
  const autoSuggestionStatus = useSelector(selectAutoSuggestionStatus);
  const [autoSuggestionVisible, setAutoSuggestionVisible] = React.useState(true);
  const [isPresetOpen, setIsPresetOpen] = React.useState<boolean>(false);

  const onFormulaPresetSelected = (preset: string) => {
    dispatch(selectFormulaPreset(preset));
    executeFormula();
  }

  const onFormulaDefinition = (formulaKeyUpEvent: React.KeyboardEvent<HTMLInputElement>): void => {
    const formula: string = formulaKeyUpEvent.currentTarget.value;
    dispatch(defineFormula(formula));
    if (formulaKeyUpEvent.key === 'Escape') {
      setAutoSuggestionVisible(false);
    } else {
      setAutoSuggestionVisible(true);
    }
    if (formulaKeyUpEvent.key === 'Enter') {
      executeFormula();
    } else {
      dispatch(suggestTokens())
        .then((resultAction) => {
          if (suggestTokens.rejected.match(resultAction)) {
            // cannot use selector here
            const autoSuggestionState: AutoSuggestionState = store.getState().autoSuggestion;
            props.addAlert(autoSuggestionState.errMessage!, 'danger');
          }
        })
    }
  }

  const executeFormula = () => {
    dispatch(validateFormula())
      .then((resultAction) => {
        if (validateFormula.fulfilled.match(resultAction)) {
          // cannot use selector here
          const formulaState: FormulaState = store.getState().formula;
          switch (formulaState.status) {
            case 'valid':
              dispatch(executeFormulaOnSamples());
              break;
            case 'invalid':
              dispatch(markSamplesAsFormulaInvalid());
              props.addAlert(formulaState.invalidMessage!, 'danger');
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
    <React.Fragment>
      <Card>
        <CardBody>
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
                <React.Fragment>
                  <InputGroup>
                    <InputGroupItem>
                      <Dropdown
                        isOpen={isPresetOpen}
                        onSelect={(event, value) => {
                          onFormulaPresetSelected(value?.toString() || '');
                          form.mutators.setInputFormulaDefinition(value);
                        }}
                        onOpenChange={(isPresetOpen: boolean) => setIsPresetOpen(isPresetOpen)}
                        toggle={(toggleRef: React.Ref<MenuToggleElement>) => (
                          <MenuToggle ref={toggleRef}
                            onClick={() => setIsPresetOpen(!isPresetOpen)}
                            isExpanded={isPresetOpen}>
                            Preset
                          </MenuToggle>
                        )}
                      >
                        <DropdownList>
                          <DropdownItem
                            value={"MUL([@[Sales Amount]],DIV([@[% Commission]],100))"}
                            key="firstPresetFormula">
                            Compute commission amount by multiplying Sales Amount by Percent Commission
                          </DropdownItem>
                          <DropdownItem
                            value={"IF(EQ([@[Sales Person]],\"Joe\"),MUL(MUL([@[Sales Amount]],DIV([@[% Commission]],100)),2),MUL([@[Sales Amount]],DIV([@[% Commission]],100)))"}
                            key="secondPresetFormula">
                            Compute commission amount by multiplying Sales Amount by Percent Commission if it is Joe mulitply by two
                          </DropdownItem>
                        </DropdownList>
                      </Dropdown>
                    </InputGroupItem>
                    <InputGroupItem isFill>
                      <Field name="formulaDefinition">
                        {props => (
                          <Popper
                            trigger={
                              <TextInput id="formulaDefinition"
                                onChange={props.input.onChange}
                                value={props.input.value}
                                onKeyUp={formulaKeyUpEvent => onFormulaDefinition(formulaKeyUpEvent)}
                                validated={
                                  (formula.status === 'notValidated' &&
                                    ValidatedOptions.default)
                                  || (formula.status === 'error' &&
                                    ValidatedOptions.error)
                                  || (formula.status === 'valid' &&
                                    ValidatedOptions.success)
                                  || (formula.status === 'validationProcessing' &&
                                    ValidatedOptions.default)
                                  || (formula.status === 'invalid' &&
                                    ValidatedOptions.error)
                                  || ValidatedOptions.default
                                }
                              />
                            }
                            popper={
                              <Menu>
                                <MenuContent>{
                                  tokens.length > 0 &&
                                  <MenuList>
                                    {
                                      tokens.map(token => {
                                        return <MenuItem key={token}>{token}</MenuItem>
                                      })
                                    }
                                  </MenuList>
                                }</MenuContent>
                              </Menu>
                            }
                            isVisible={autoSuggestionStatus === 'idle' && autoSuggestionVisible}
                          />
                        )}
                      </Field>
                    </InputGroupItem>
                  </InputGroup>
                </React.Fragment>
              )} />
          </FormGroup>
        </CardBody>
      </Card>
    </React.Fragment>
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
    <Card>
      <CardBody>
        <Table variant='compact'>
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
      </CardBody>
    </Card>
  )
}

const ExecutionDebug: React.FunctionComponent<{}> = () => {
  const formula = useSelector(selectFormula);
  const samples = useSelector(selectSamples);
  const executionsDebug = useSelector(selectExecutionsDebug);
  const dispatch = useAppDispatch();
  const columnNames = {
    executedAtStart: 'Executed At Start',
    executedAtEnd: 'Executed At End',
    inputs: 'Inputs',
    result: 'Result',
    underline: 'Underline'
  };

  const executionsDebugSelected = useSelector((state: RootState) => 
    state.executionsDebug.executions.filter(executions => executions.salesPerson === state.executionsDebug.selectedSalesPerson)
      .flatMap(executions => executions.executionsResult)
  );

  const result: string | null = executionsDebugSelected.length > 0 ? executionsDebugSelected[0].result : null;
  const processedInMillis: number | null = executionsDebugSelected.length > 0 ? executionsDebugSelected[0].processedInNanos / 1000000 : null;

  return (
    <React.Fragment>
      <Card>
        <CardHeader>
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
        </CardHeader>
        <CardBody>
          <Stack>
            <StackItem>
              <Flex spaceItems={{ default: 'spaceItemsMd' }}>
                <FlexItem>
                  <CheckCircleIcon /> {result}
                </FlexItem>
                <FlexItem>
                  <OutlinedClockIcon /> {processedInMillis !== null &&
                    `${processedInMillis} milliseconds`} 
                </FlexItem>
              </Flex>
            </StackItem>
            <StackItem>
              <Table variant='compact'>
                <Thead>
                  <Tr>
                    <Td>{columnNames.underline}</Td>
                    <Td>{columnNames.inputs}</Td>
                    <Td>{columnNames.result}</Td>
                    <Td>{columnNames.executedAtStart}</Td>
                    <Td>{columnNames.executedAtEnd}</Td>
                  </Tr>
                </Thead>
                <Tbody>
                  {executionsDebugSelected
                    .flatMap(executionsResult => executionsResult.executions)
                    .map((execution: ExecutionDTO, index: number) => {
                      return (
                        <Tr key={index}>
                          <Td dataLabel={columnNames.underline}>
                            {
                              formula.formula.substring(0, execution.position!.start!)
                            }
                            <b>
                              {
                                formula.formula.substring(execution.position!.start!, execution.position!.end! + 1)
                              }
                            </b>
                            {
                              formula.formula.substring(execution.position!.end! + 1, formula.formula.length)
                            }
                          </Td>
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
                          <Td dataLabel={columnNames.executedAtStart}>{execution.executedAtStart}</Td>
                          <Td dataLabel={columnNames.executedAtEnd}>{execution.executedAtEnd}</Td>
                        </Tr>
                      )
                    })}
                </Tbody>
              </Table>
            </StackItem>
          </Stack>
        </CardBody>
      </Card>
    </React.Fragment>
  );
}


function App() {
  const [alerts, setAlerts] = React.useState<Partial<AlertProps>[]>([]);

  const getUniqueId = () => new Date().getTime();

  const addAlert = (title: string, variant: AlertProps['variant']) => {
    const key: React.Key = getUniqueId();
    setAlerts(prevAlerts => [...prevAlerts, { title, variant, key }]);
  };

  const removeAlert = (key: React.Key) => {
    setAlerts(prevAlerts => [...prevAlerts.filter(alert => alert.key !== key)]);
  };

  return (
    <React.Fragment>
      <Page>
        <PageSection isWidthLimited isCenterAligned>
          <Grid hasGutter>
            <GridItem span={12}><Formula addAlert={addAlert} /></GridItem>
            <GridItem span={12}><Samples /></GridItem>
            <GridItem span={12}><ExecutionDebug /></GridItem>
          </Grid>
        </PageSection>
      </Page>
      <AlertGroup isToast isLiveRegion>
        {alerts.map(({ key, variant, title }) => (
          <Alert
            variant={AlertVariant[variant!]}
            title={title}
            timeout={5000}
            onTimeout={() => removeAlert(key!)}
            isLiveRegion
            actionClose={
              <AlertActionCloseButton
                variantLabel={`${variant} alert`} onClose={() => removeAlert(key!)} />
            }
            key={key}
          />
        ))}
      </AlertGroup>
    </React.Fragment>
  );
};

export default App;
