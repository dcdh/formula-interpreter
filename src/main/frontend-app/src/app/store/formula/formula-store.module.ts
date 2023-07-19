import { NgModule } from '@angular/core';

import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';

import { FormulaEffects } from './formula.effects';
import { formulaReducer } from './formula.reducers';

@NgModule({
    imports: [
        StoreModule.forFeature('formula', formulaReducer),
        EffectsModule.forFeature([FormulaEffects])
    ]
})
export class BookStoreModule {}
