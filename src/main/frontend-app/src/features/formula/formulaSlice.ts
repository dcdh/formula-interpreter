import { createAsyncThunk, createSlice, PayloadAction } from '@reduxjs/toolkit';
import { RootState, store } from '../../app/store';
import { SyntaxErrorDTO, ValidatorEndpointApi } from '../../generated';
import { AjaxError } from 'rxjs/ajax';
import { firstValueFrom } from 'rxjs';

export interface FormulaState {
  formula: string;
  invalidMessage: string | null;
  errorMessage: string | null;
  status: 'notValidated' | 'validationProcessing' | 'valid' | 'invalid' | 'error';
};

const initialFormulaState: FormulaState = {
  formula: '',
  invalidMessage: null,
  errorMessage: null,
  status: 'notValidated'
};

interface RemoteError {
  message: string;
};

export const validateFormula = createAsyncThunk<
  SyntaxErrorDTO | null, void,
  {
    rejectValue: RemoteError
  }>('formula/validate', async (_void: void, { rejectWithValue }) => {
    const validator = new ValidatorEndpointApi();
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
    }
  },
  extraReducers: (builder) => {
    builder
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

export const { selectFormulaPreset, defineFormula } = formulaSlice.actions;

export const selectFormula = (state: RootState) => state.formula;

export const selectAutoSuggestionStatus = (state: RootState) => state.autoSuggestion.status;

export default formulaSlice.reducer;