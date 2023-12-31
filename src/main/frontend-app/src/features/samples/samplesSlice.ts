import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { RootState, store } from '../../app/store';
import { DebugFeature, ElementExecution, ExecutionResult, ExecutorEndpointApi } from "../../generated";
import { AjaxError } from 'rxjs/ajax';
import { firstValueFrom } from 'rxjs';
import { Input } from '../../generated/models/Input';

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
  executedAtStart: string;
  executedAtEnd: string;
  processedInNanos: number;
  result: string;
  elementExecutions: Array<ElementExecutionState>;
};

export interface InputState {
  range: RangeState;
  name: string;
  value: string;
};

export interface ElementExecutionState {
  executedAtStart: string;
  executedAtEnd: string;
  processedInNanos: number;
  range: RangeState;
  inputs: Array<InputState>;
  result: string;
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
>('samples/executeFormula', async (_void: void) => {
  const formula: string = store.getState().formula.formula;
  const debugFeature: DebugFeature = store.getState().formula.debugFeature;
  const samples: SampleState[] = store.getState().samples.samples;
  let status: 'notExecutedYet' | 'executed' | 'processing' | 'failed' | 'formulaInError' | 'formulaInvalid' = 'notExecutedYet';
  let commissionAmount: string | null = null;
  let executions: ExecutionsResultState | null = null;
  const results: SampleState[] = await Promise.all(samples.map(async function (sample: SampleState) {
    const executor = new ExecutorEndpointApi();
    try {
      const executionResult: ExecutionResult = await firstValueFrom(executor.execute({
        execute: {
          formula: formula,
          structuredData: {
            'Sales Person': sample.salesPerson,
            'Region': sample.region,
            'Sales Amount': sample.salesAmount.toString(),
            '% Commission': sample.percentCommission.toString()
          },
          debugFeature: debugFeature
        }
      }));
      status = 'executed';
      commissionAmount = executionResult.result!;
      executions = {
        result: executionResult.result,
        executedAtStart: executionResult.executedAtStart,
        executedAtEnd: executionResult.executedAtEnd,
        processedInNanos: executionResult.processedInNanos,
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