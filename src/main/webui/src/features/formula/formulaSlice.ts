import { createAsyncThunk, createSlice, PayloadAction } from '@reduxjs/toolkit';
import { RootState } from '../../app/store';
import { AjaxError } from 'rxjs/ajax';
import { DebugFeature, Validate200 } from '../../api/model';
import { validate } from '../../api/endpoints';

export interface FormulaState {
  formula: string;
  debugFeature: DebugFeature; 
  invalidMessage: string | null;
  errorMessage: string | null;
  status: 'notValidated' | 'validationProcessing' | 'valid' | 'invalid' | 'error';
};

const initialFormulaState: FormulaState = {
  formula: '',
  debugFeature: DebugFeature.ACTIVE,
  invalidMessage: null,
  errorMessage: null,
  status: 'notValidated'
};

interface RemoteError {
  message: string;
};

export const validateFormula = createAsyncThunk<
  Validate200,
  void,
  {
    rejectValue: RemoteError
  }>('formula/validate', (_void: void, { getState, rejectWithValue }) => {
    const state = getState() as RootState;
    return validate({ formula: state.formula.formula })
      .then(response => response as Validate200)
      .catch(error => {
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
      });
  });

export const formulaSlice = createSlice({
  name: 'formula',
  initialState: initialFormulaState,
  reducers: {
    selectFormulaPreset: (state, action: PayloadAction<string>) => {
      state.formula = action.payload;
      state.status = 'notValidated';
      state.invalidMessage = null;
      state.errorMessage = null;
    },
    defineFormula: (state, action: PayloadAction<string>) => {
      state.formula = action.payload;
      state.status = 'notValidated';
      state.invalidMessage = null;
      state.errorMessage = null;
    },
    activeDebug: (state) => {
      state.debugFeature = DebugFeature.ACTIVE;
    },
    inactiveDebug: (state) => {
      state.debugFeature = DebugFeature.INACTIVE;
    }
  },
  extraReducers: (builder) => {
    builder
      .addCase(validateFormula.rejected, (state, { payload }) => {
        state.status = 'error';
        state.invalidMessage = null;
        state.errorMessage = payload!.message;
      })
      .addCase(validateFormula.pending, () => {
        /**
         * Avoid flickering because it is really fast
        state.status = 'validationProcessing';
        state.invalidMessage = null;
        state.errorMessage = null;
        */
      })
      .addCase(validateFormula.fulfilled, (state, { payload }) => {
        if (payload.valid === true) {
          state.status = 'valid';
          state.invalidMessage = null;
          state.errorMessage = null;
        } else {
          state.status = 'invalid';
          state.invalidMessage = payload?.msg || 'something wrong happened';
          state.errorMessage = null;
        }
      });
  },
});

export const { selectFormulaPreset, defineFormula, activeDebug, inactiveDebug } = formulaSlice.actions;

export const selectFormula = (state: RootState) => state.formula;

export const selectAutoSuggestionStatus = (state: RootState) => state.autoSuggestion.status;

export default formulaSlice.reducer;