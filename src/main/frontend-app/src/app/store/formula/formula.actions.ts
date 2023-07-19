import { createAction, props } from '@ngrx/store';

const prefix = '[Formula]';

export const defineFormula = createAction(
    `${prefix} Define Formula`,
    props<{
        formula: string;
    }>()
);

export const suggestCompletion = createAction(
    `${prefix} Suggest Completion`,
    props<{
        suggestedFormula: string;
    }>()
);

export const suggestedCompletionTokens = createAction(
    `${prefix} Suggest Completion Tokens`,
    props<{
        tokens: string[];
    }>()
);

// faire mes putains d'exceptions !!!!
// et ces exceptions sont mappées en fonction du content type ...
ha flute je dois certainement indiqué les types d'erreurs que je peux produire avec les content types !!! pour openapi !!!
export const suggestedCompletion

// defineFormula
// submitFormula
// -> validate it
