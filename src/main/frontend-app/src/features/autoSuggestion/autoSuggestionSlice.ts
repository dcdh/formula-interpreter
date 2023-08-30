import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { RootState, store } from '../../app/store';
import { SuggestCompletionEndpointApi } from "../../generated";
import { AjaxError } from 'rxjs/ajax';
import { firstValueFrom } from 'rxjs';

export interface AutoSuggestionState {
  tokens: string[];
  errMessage: string | null;
  status: 'idle' | 'loading' | 'failed';
};

const initialAutoSuggestionState: AutoSuggestionState = {
  tokens: [],
  errMessage: null,
  status: 'idle'
};

interface RemoteError {
  message: string;
};

export const suggestTokens = createAsyncThunk<
  string[], void,
  {
    rejectValue: RemoteError
  }>('autoSuggestion/suggestTokens', async (_void: void, { rejectWithValue }) => {
    const suggest = new SuggestCompletionEndpointApi();
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

export const autoSuggestionSlice = createSlice({
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
        /**
         * Avoid flickering because it is really fast
        state.status = 'loading';
        state.errMessage = null;
        state.tokens = [];
        */
      })
      .addCase(suggestTokens.fulfilled, (state, { payload }) => {
        state.status = 'idle';
        state.errMessage = null;
        state.tokens = payload;
      });
  },
});

export const selectTokens = (state: RootState) => state.autoSuggestion.tokens;

export default autoSuggestionSlice.reducer;