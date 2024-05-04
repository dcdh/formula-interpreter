import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { RootState } from '../../app/store';
import { executeFormulaOnSamples, ExecutionsResultState, markSamplesAsFormulaInError, markSamplesAsFormulaInvalid, SampleState } from '../samples/samplesSlice';
import { createSelector } from 'redux-views';

export interface ExecutionsStateBySalesPerson {
  salesPerson: string;
  executionsResult: ExecutionsResultState;
}

export interface ExecutionsDebugState {
  selectedSalesPerson: string;
  executionsBySalesPerson: Array<ExecutionsStateBySalesPerson>;
}

const initialExecutionsDebugState: ExecutionsDebugState = {
  selectedSalesPerson: 'Joe',
  executionsBySalesPerson: []
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
        state.executionsBySalesPerson = []
      })
      .addCase(executeFormulaOnSamples.pending, () => {
        /**
         * Avoid flickering because it is really fast
        state.executions = []
         */
      })
      .addCase(executeFormulaOnSamples.fulfilled, (state, { payload }) => {
        state.executionsBySalesPerson = payload.map((sample: SampleState) => {
          return {
            salesPerson: sample.salesPerson,
            executionsResult: sample.executions!
          }
        })
      })
      .addCase(markSamplesAsFormulaInError, (state) => {
        state.executionsBySalesPerson = []
      })
      .addCase(markSamplesAsFormulaInvalid, (state) => {
        state.executionsBySalesPerson = []
      })
  }
});

export const { selectSalesPerson } = executionsDebugSlice.actions;

export const selectExecutionsDebug = (state: RootState) => state.executionsDebug;

// https://redux.js.org/usage/deriving-data-selectors#optimizing-selectors-with-memoization
// need to use the memoization due to the flatMap changing the returned reference
export const selectExecutionsDebugSelected = createSelector(
  [
    (state: RootState) => state.executionsDebug.executionsBySalesPerson,
    (state: RootState) => state.executionsDebug.selectedSalesPerson
  ],
  (executionsStateBySalesPerson: Array<ExecutionsStateBySalesPerson>, selectedSalesPerson: string) => {
    return executionsStateBySalesPerson.find(executions => executions.salesPerson === selectedSalesPerson)
  });

export default executionsDebugSlice.reducer;