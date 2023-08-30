import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { RootState } from '../../app/store';
import { executeFormulaOnSamples, ExecutionsResultState, markSamplesAsFormulaInError, markSamplesAsFormulaInvalid, SampleState } from '../samples/samplesSlice';
import { createSelector } from 'redux-views';

export interface ExecutionsState {
  salesPerson: string;
  executionsResult: ExecutionsResultState;
}

export interface ExecutionsDebugState {
  selectedSalesPerson: string | null;
  executions: Array<ExecutionsState>;
}

const initialExecutionsDebugState: ExecutionsDebugState = {
  selectedSalesPerson: null,
  executions: []
}

export const executionsDebugSlice = createSlice({
  name: 'executionsDebug',
  initialState: initialExecutionsDebugState,
  reducers: {
    selectSalesPerson: (state, action: PayloadAction<string>) => {
      state.selectedSalesPerson = action.payload;
    }
  },
  extraReducers: (builder) => {
    builder
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
});

export const { selectSalesPerson } = executionsDebugSlice.actions;

export const selectExecutionsDebug = (state: RootState) => state.executionsDebug;

// https://redux.js.org/usage/deriving-data-selectors#optimizing-selectors-with-memoization
// need to use the memoization due to the flatMap changing the returned reference
export const selectExecutionsDebugSelected = createSelector(
  [
    (state: RootState) => state.executionsDebug.executions,
    (state: RootState) => state.executionsDebug.selectedSalesPerson
  ],
  (executions: Array<ExecutionsState>, selectedSalesPerson: string | null) => {
    return executions.filter(executions => executions.salesPerson === selectedSalesPerson)
      .flatMap(executions => executions.executionsResult);
  });

export default executionsDebugSlice.reducer;