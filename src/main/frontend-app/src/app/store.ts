import { configureStore, ThunkAction, Action } from '@reduxjs/toolkit';
import formulaReducer from '../features/formula/formulaSlice';
import autoSuggestionReducer from '../features/autoSuggestion/autoSuggestionSlice';
import samplesReducer from '../features/samples/samplesSlice';
import executionsDebugReducer from '../features/executionsDebug/executionsDebugSlice';

export const store = configureStore({
    reducer: {
        formula: formulaReducer,
        autoSuggestion: autoSuggestionReducer,
        samples: samplesReducer,
        executionsDebug: executionsDebugReducer
    },
});

export type AppDispatch = typeof store.dispatch;
export type RootState = ReturnType<typeof store.getState>;
export type AppThunk<ReturnType = void> = ThunkAction<
    ReturnType,
    RootState,
    unknown,
    Action<string>
>;