import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { RootState } from '../../app/store';
import { AjaxError } from 'rxjs/ajax';
import { DebugFeature, ElementExecution, ExecutionResult, Input } from '../../api/model';
import { execute } from '../../api/endpoints';

export interface SampleState {
  salesPerson: string;
  region: string;
  salesAmount: number;
  percentCommission: number;
  commissionAmount: string | null;
  status: 'notExecutedYet' | 'executed' | 'processing' | 'failed' | 'formulaInError' | 'formulaInvalid';
  executions: ExecutionsResultState | null;
};

export interface ExecutionsResultState {
  exactProcessedInNanos: number;
  executionProcessedIn: ExecutionProcessedInState;
  parserExecutionProcessedIn? : ParserExecutionProcessedInState;
  result: string;
  elementExecutions: Array<ElementExecutionState>;
};

export interface InputState {
  range: RangeState;
  name: string;
  value: string;
};

export interface ElementExecutionState {
  processedInNanos: number;
  range: RangeState;
  inputs: Array<InputState>;
  result: string;
};

export interface ExecutionProcessedInState {
  processedInNanos: number;
};

export interface ParserExecutionProcessedInState {
  processedInNanos: number;
};

export interface RangeState {
  start: number;
  end: number;
};

export interface SamplesState {
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

export const executeFormulaOnSamples = createAsyncThunk<SampleState[], void
>('samples/executeFormula', async (_void: void, { getState }) => {
  const state = getState() as RootState;
  const formula: string = state.formula.formula;
  const debugFeature: DebugFeature = state.formula.debugFeature;
  const samples: SampleState[] = state.samples.samples;
  let status: 'notExecutedYet' | 'executed' | 'processing' | 'failed' | 'formulaInError' | 'formulaInvalid' = 'notExecutedYet';
  let commissionAmount: string | null = null;
  let executions: ExecutionsResultState | null = null;
  return await Promise.all(samples.map(async function (sample: SampleState) {
    return await execute({
      formula: formula,
      structuredReferences: {
        'Sales Person': sample.salesPerson,
        'Region': sample.region,
        'Sales Amount': sample.salesAmount.toString(),
        '% Commission': sample.percentCommission.toString()
      },
      debugFeature: debugFeature
    })
    .then(response => response as ExecutionResult)
    .then(executionResult => {
      status = 'executed';
      commissionAmount = executionResult.result!;
      executions = {
        result: executionResult.result,
        parserExecutionProcessedIn: executionResult.parserExecutionProcessedIn,
        executionProcessedIn: executionResult.executionProcessedIn,
        exactProcessedInNanos: executionResult.exactProcessedInNanos,
        elementExecutions: executionResult.elementExecutions.map((elementExecution: ElementExecution) => {
          return {
            executedAtStart: elementExecution.executedAtStart,
            executedAtEnd: elementExecution.executedAtEnd,
            processedInNanos: elementExecution.processedInNanos,
            range: {
              start: elementExecution.range.start,
              end: elementExecution.range.end
            },
            inputs: elementExecution.inputs.map((input: Input) => {
              return {
                name: input.name,
                value: input.value,
                range: {
                  start: input.range.start,
                  end: input.range.end
                }
              }
            }),
            result: elementExecution.result
          }
        })
      };
      return {
        salesPerson: sample.salesPerson,
        region: sample.region,
        salesAmount: sample.salesAmount,
        percentCommission: sample.percentCommission,
        commissionAmount,
        status,
        executions
      };
    })
    .catch(error => {
      status = 'failed';
      if (error instanceof AjaxError) {
        switch (error.xhr.getResponseHeader('content-type')) {
          case 'application/vnd.evaluation-syntax-error-v1+json':
            // TODO
            break;
          case 'application/vnd.evaluation-unexpected-exception-v1+json':
            // TODO
            break;
          default:
            // TODO
            break;
        }
      } else {
        // TODO
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
    });
  }));
});

export const samplesSlice = createSlice({
  name: 'samples',
  initialState: initialSamplesState,
  reducers: {
    markSamplesAsFormulaInError: (state) => {
      state.samples.forEach(sample => {
        sample.status = 'formulaInError';
      });
    },
    markSamplesAsFormulaInvalid: (state) => {
      state.samples.forEach(sample => {
        sample.status = 'formulaInvalid';
      });
    }
  },
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
  }
});

export const { markSamplesAsFormulaInError, markSamplesAsFormulaInvalid } = samplesSlice.actions;

export const selectSamples = (state: RootState) => state.samples;

export default samplesSlice.reducer;