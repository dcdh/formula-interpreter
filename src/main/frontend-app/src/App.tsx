import React from 'react';
import '@patternfly/react-core/dist/styles/base.css';
import {
  Card, CardBody,
  Page, PageSection,
  Alert, FormAlert,
  FormGroup, TextInput,
  Stack, StackItem, FormHelperText, HelperText, HelperTextItem, Label, Spinner
} from '@patternfly/react-core';
import { TableComposable, Thead, Tr, Th, Tbody, Td } from '@patternfly/react-table';
import { configureStore, createAction } from '@reduxjs/toolkit';
import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { useDispatch, useSelector } from 'react-redux';

import { ValidatorEndpointApi, SuggestCompletionEndpointApi, ExecutorEndpointApi, SyntaxErrorDTO, ExecutionResultDTO } from './generated';
import { firstValueFrom } from 'rxjs';
import { AjaxError } from 'rxjs/ajax';

const validator = new ValidatorEndpointApi();
const suggest = new SuggestCompletionEndpointApi();
const executor = new ExecutorEndpointApi();

const useAppDispatch: () => AppDispatch = useDispatch

interface SampleItem {
  salesPerson: string,
  region: string,
  salesAmount: number,
  percentCommission: number,
  commissionAmount: string | null,
  status: 'notExecutedYet' | 'executed' | 'processing' | 'failed' | 'formulaInError' | 'formulaInvalid'
};

const columnNames = {
  salesPerson: 'Sales Person',
  region: 'Region',
  salesAmount: 'Sales Amount',
  percentCommission: '% Commission',
  commissionAmount: 'Commission Amount'
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

interface SampleState {
  items: SampleItem[];
};

const initialSampleState: SampleState = {
  items: [
    { salesPerson: 'Joe', region: 'North', salesAmount: 260, percentCommission: 10, commissionAmount: null, status: 'notExecutedYet' },
    { salesPerson: 'Robert', region: 'South', salesAmount: 660, percentCommission: 15, commissionAmount: null, status: 'notExecutedYet' },
    { salesPerson: 'Michelle', region: 'East', salesAmount: 940, percentCommission: 15, commissionAmount: null, status: 'notExecutedYet' },
    { salesPerson: 'Erich', region: 'West', salesAmount: 410, percentCommission: 12, commissionAmount: null, status: 'notExecutedYet' },
    { salesPerson: 'Dafna', region: 'North', salesAmount: 800, percentCommission: 15, commissionAmount: null, status: 'notExecutedYet' },
    { salesPerson: 'Rob', region: 'South', salesAmount: 900, percentCommission: 15, commissionAmount: null, status: 'notExecutedYet' }
  ]
};

const defineFormula = createAction<string>('formula/define');

interface RemoteError {
  message: string
}

const validateFormula = createAsyncThunk<
  SyntaxErrorDTO | null, string,
  {
    rejectValue: RemoteError
  }>('formula/validate', async (formula: string, { rejectWithValue }) => {
    try {
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
  string[], string,
  {
    rejectValue: RemoteError
  }>('autoSuggestion/suggestTokens', async (formula: string, { rejectWithValue }) => {
    try {
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

type SampleToApplyFormulaOn = {
  formula: string,
  sampleItem: SampleItem,
  sampleIndex: number
};

const executeFormulaOnSamples = createAsyncThunk<ExecutionResultDTO, SampleToApplyFormulaOn,
  {
    rejectValue: RemoteError
  }
>('samples/executeFormula', async (sampleToApplyFormulaOn: SampleToApplyFormulaOn, { rejectWithValue }) => {
  const { formula, sampleItem } = sampleToApplyFormulaOn;
  try {
    const executionResult: ExecutionResultDTO = await firstValueFrom(executor.execute({
      executeDTO: {
        formula: formula,
        structuredData: {
          'Sales Person': sampleItem.salesPerson,
          'Region': sampleItem.region,
          'Sales Amount': sampleItem.salesAmount.toString(),
          '% Commission': sampleItem.percentCommission.toString()
        }
      }
    }))
    return executionResult;
  } catch (error) {
    if (error instanceof AjaxError) {
      switch (error.xhr.getResponseHeader('content-type')) {
        case 'application/vnd.execution-syntax-error-v1+json':
          throw rejectWithValue({
            message: error.response.message
          });
        case 'application/vnd.execution-unexpected-exception-v1+json':
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

const markSamplesAsFormulaInError = createAction('samples/markAsFormulaInError');

const markSamplesAsFormulaInvalid = createAction('samples/markAsFormulaInvalid');


const sampleSlice = createSlice({
  name: 'sample',
  initialState: initialSampleState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(executeFormulaOnSamples.rejected, (state, { meta }) => {
        const sampleItem = state.items[meta.arg.sampleIndex];
        sampleItem.status = 'failed';
        sampleItem.commissionAmount = null;
      })
      .addCase(executeFormulaOnSamples.pending, (state, { meta }) => {
        const sampleItem = state.items[meta.arg.sampleIndex];
        sampleItem.status = 'processing';
        sampleItem.commissionAmount = null;
      })
      .addCase(executeFormulaOnSamples.fulfilled, (state, { meta, payload }) => {
        const sampleItem = state.items[meta.arg.sampleIndex];
        sampleItem.status = 'executed';
        sampleItem.commissionAmount = payload.result!;
      })
      .addCase(markSamplesAsFormulaInError, (state) => {
        state.items.forEach(item => {
          item.status = 'formulaInError';
        });
      })
      .addCase(markSamplesAsFormulaInvalid, (state) => {
        state.items.forEach(item => {
          item.status = 'formulaInvalid';
        });
      })
  }
})

const selectSample = (state: RootState) => state.sample;
const selectFormula = (state: RootState) => state.formula;
const selectTokens = (state: RootState) => state.autoSuggestion.tokens;
const selectAutoSuggestionStatus = (state: RootState) => state.autoSuggestion.status;
const selectAutoSuggestionErrMessage = (state: RootState) => state.autoSuggestion.errMessage;

export const store = configureStore({
  reducer: {
    formula: formulaSlice.reducer,
    autoSuggestion: autoSuggestionSlice.reducer,
    sample: sampleSlice.reducer
  },
});

type RootState = ReturnType<typeof store.getState>;
type AppDispatch = typeof store.dispatch;

function App() {
  const dispatch = useAppDispatch();
  const formula = useSelector(selectFormula);
  const sample = useSelector(selectSample);
  const tokens = useSelector(selectTokens);
  const autoSuggestionStatus = useSelector(selectAutoSuggestionStatus);
  const autoSuggestionErrMessage = useSelector(selectAutoSuggestionErrMessage);
  return (
    <Page>
      <PageSection isWidthLimited isCenterAligned>
        <Card>
          <CardBody>
            <Stack hasGutter>
              <StackItem>
              MUL([@[Sales Amount]],[@[% Commission]])
              IF(EQ([@[Sales Person]],"Joe"),MUL(MUL([@[Sales Amount]],[@[% Commission]]),2),MUL([@[Sales Amount]],[@[% Commission]]))
              </StackItem>
              <StackItem>
                <FormGroup>
                  <TextInput id="formulaDefinition"
                    onKeyUp={formulaKeyUpEvent => onFormulaDefinition(formulaKeyUpEvent)}
                  />
                  <FormHelperText isHidden={false} component="div">
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
                </FormGroup>
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
                  {formula.status === 'notValidated' &&
                    <Alert variant="info" title="Not validated yet." aria-live="polite" isInline />
                  }
                </FormAlert>
              </StackItem>
              <StackItem isFilled>
                <TableComposable aria-label="Actions table">
                  <Thead>
                    <Tr>
                      <Th>{columnNames.salesPerson}</Th>
                      <Th>{columnNames.region}</Th>
                      <Th>{columnNames.salesAmount}</Th>
                      <Th>{columnNames.percentCommission}</Th>
                      <Th>{columnNames.commissionAmount}</Th>
                      <Td></Td>
                    </Tr>
                  </Thead>
                  <Tbody>
                    {sample.items.map(item => (
                      <Tr key={item.salesPerson}>
                        <Td dataLabel={columnNames.salesPerson}>{item.salesPerson}</Td>
                        <Td dataLabel={columnNames.region}>{item.region}</Td>
                        <Td dataLabel={columnNames.salesAmount}>{item.salesAmount}</Td>
                        <Td dataLabel={columnNames.percentCommission}>{item.percentCommission}</Td>
                        <Td dataLabel={columnNames.commissionAmount}>
                          {item.status === 'notExecutedYet' &&
                            <Label color="blue">Not executed yet</Label>
                          }
                          {item.status === 'executed' &&
                            <Label color="green">{item.commissionAmount}</Label>
                          }
                          {item.status === 'processing' &&
                            <Spinner size="sm" />
                          }
                          {item.status === 'failed' &&
                            <Label color="red">Somethings wrong happened</Label>
                          }
                          {item.status === 'formulaInError' &&
                            <Label color="red">Formula in error unable to process</Label>
                          }
                          {item.status === 'formulaInvalid' &&
                            <Label color="orange">Formula invalid</Label>
                          }
                        </Td>
                      </Tr>
                    ))}
                  </Tbody>
                </TableComposable>
              </StackItem>
            </Stack>
          </CardBody>
        </Card>
      </PageSection>
    </Page>
  );

  function onFormulaDefinition(formulaKeyUpEvent: React.KeyboardEvent<HTMLInputElement>): void {
    const formula: string = formulaKeyUpEvent.currentTarget.value;
    if (formulaKeyUpEvent.key === 'Enter') {
      dispatch(validateFormula(formula))
        .then((resultAction) => {
          if (validateFormula.fulfilled.match(resultAction)) {
            switch (store.getState().formula.status) {
              case 'valid':
                sample.items.forEach(function (item, index) {
                  dispatch(executeFormulaOnSamples({
                    formula: formula,
                    sampleItem: item,
                    sampleIndex: index
                  }));
                });
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
        })
    } else {
      dispatch(defineFormula(formula));
      dispatch(suggestTokens(formula));
    }
  }
};

export default App;
