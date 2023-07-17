import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { FormulaInputComponent } from './formula-input/formula-input.component';
import { SampleComponent } from './sample/sample.component';

@NgModule({
  declarations: [
    AppComponent,
    FormulaInputComponent,
    SampleComponent
  ],
  imports: [
    BrowserModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
