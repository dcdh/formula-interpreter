import React from 'react';

import { useAppSelector, useAppDispatch } from '../../app/hooks';
import { store } from '../../app/store';
import * as Core from '@patternfly/react-core';
import * as FinalForm from 'react-final-form';
import * as FinalFormListeners from 'react-final-form-listeners';
import {
  FormulaState,
  defineFormula,
  selectAutoSuggestionStatus,
  selectFormula,
  selectFormulaPreset,
  validateFormula,
  activeDebug,
  inactiveDebug
} from './formulaSlice';
import { AutoSuggestionState, suggestTokens } from '../autoSuggestion/autoSuggestionSlice';
import { executeFormulaOnSamples, markSamplesAsFormulaInError, markSamplesAsFormulaInvalid } from '../samples/samplesSlice';
import { AutoSuggestion } from '../autoSuggestion/AutoSuggestion';

export function Formula(props: { addAlert: (title: string, variant: Core.AlertProps['variant']) => void; }) {
  const dispatch = useAppDispatch();
  const formula = useAppSelector(selectFormula);
  const autoSuggestionStatus = useAppSelector(selectAutoSuggestionStatus);

  const [autoSuggestionVisible, setAutoSuggestionVisible] = React.useState(true);
  const [isPresetOpen, setIsPresetOpen] = React.useState<boolean>(false);

  const onFormulaPresetSelected = (preset: string) => {
    dispatch(selectFormulaPreset(preset));
    executeFormula();
  }

  const onFormulaDefinition = (formulaKeyUpEvent: React.KeyboardEvent<HTMLInputElement>): void => {
    const formula: string = formulaKeyUpEvent.currentTarget.value;
    dispatch(defineFormula(formula));
    if (formulaKeyUpEvent.key === 'Escape') {
      setAutoSuggestionVisible(false);
    } else {
      setAutoSuggestionVisible(true);
    }
    if (formulaKeyUpEvent.key === 'Enter') {
      executeFormula();
    } else {
      dispatch(suggestTokens())
        .then((resultAction) => {
          if (suggestTokens.rejected.match(resultAction)) {
            // cannot use selector here
            const autoSuggestionState: AutoSuggestionState = store.getState().autoSuggestion;
            props.addAlert(autoSuggestionState.errMessage!, 'danger');
          }
        })
    }
  }

  const executeFormula = () => {
    dispatch(validateFormula())
      .then((resultAction) => {
        if (validateFormula.fulfilled.match(resultAction)) {
          // cannot use selector here
          const formulaState: FormulaState = store.getState().formula;
          switch (formulaState.status) {
            case 'valid':
              dispatch(executeFormulaOnSamples());
              break;
            case 'invalid':
              dispatch(markSamplesAsFormulaInvalid());
              props.addAlert(formulaState.invalidMessage!, 'danger');
              break;
            default:
              console.log('Should not be here');
          }
        } else {
          dispatch(markSamplesAsFormulaInError());
        }
      });
  }

  return (
    <React.Fragment>
      <Core.Card>
        <Core.CardBody>
          <Core.FormGroup>
            <FinalForm.Form
              initialValues={{
                formulaDefinition: '',
                debugFeature: true
              }}
              mutators={{
                setInputFormulaDefinition: (formulaDefinition: string, state, utils) => {
                  utils.changeValue(state, 'formulaDefinition', () => formulaDefinition)
                }
              }}
              onSubmit={() => { }}
              render={({ form }) => (
                <React.Fragment>
                  <Core.Stack hasGutter>
                    <Core.StackItem>
                      <Core.InputGroup>
                        <Core.InputGroupItem>
                          <Core.Dropdown
                            isOpen={isPresetOpen}
                            onSelect={(event, value) => {
                              onFormulaPresetSelected(value?.toString() || '');
                              form.mutators.setInputFormulaDefinition(value);
                            }}
                            onOpenChange={(isPresetOpen: boolean) => setIsPresetOpen(isPresetOpen)}
                            toggle={(toggleRef: React.Ref<Core.MenuToggleElement>) => (
                              <Core.MenuToggle ref={toggleRef}
                                onClick={() => setIsPresetOpen(!isPresetOpen)}
                                isExpanded={isPresetOpen}>
                                Preset
                              </Core.MenuToggle>
                            )}
                          >
                            <Core.DropdownList>
                              <Core.DropdownItem
                                value={"MUL([@[Sales Amount]],DIV([@[% Commission]],100))"}
                                key="firstPresetFormula">
                                Compute commission amount by multiplying Sales Amount by Percent Commission
                              </Core.DropdownItem>
                              <Core.DropdownItem
                                value={"IF(EQ([@[Sales Person]],\"Joe\"),MUL(MUL([@[Sales Amount]],DIV([@[% Commission]],100)),2),MUL([@[Sales Amount]],DIV([@[% Commission]],100)))"}
                                key="secondPresetFormula">
                                Compute commission amount by multiplying Sales Amount by Percent Commission if it is Joe mulitply by two
                              </Core.DropdownItem>
                            </Core.DropdownList>
                          </Core.Dropdown>
                        </Core.InputGroupItem>
                        <Core.InputGroupItem isFill>
                          <FinalForm.Field name="formulaDefinition">
                            {props => (
                              <Core.Popper
                                trigger={
                                  <Core.TextInput id="formulaDefinition"
                                    onChange={props.input.onChange}
                                    value={props.input.value}
                                    onKeyUp={formulaKeyUpEvent => onFormulaDefinition(formulaKeyUpEvent)}
                                    validated={
                                      (formula.status === 'notValidated' &&
                                        Core.ValidatedOptions.default)
                                      || (formula.status === 'error' &&
                                        Core.ValidatedOptions.error)
                                      || (formula.status === 'valid' &&
                                        Core.ValidatedOptions.success)
                                      || (formula.status === 'validationProcessing' &&
                                        Core.ValidatedOptions.default)
                                      || (formula.status === 'invalid' &&
                                        Core.ValidatedOptions.error)
                                      || Core.ValidatedOptions.default
                                    }
                                  />
                                }
                                popper={
                                  <Core.Menu>
                                    <Core.MenuContent>
                                      <AutoSuggestion />
                                    </Core.MenuContent>
                                  </Core.Menu>
                                }
                                isVisible={autoSuggestionStatus === 'idle' && autoSuggestionVisible}
                              />
                            )}
                          </FinalForm.Field>
                        </Core.InputGroupItem>
                      </Core.InputGroup>
                    </Core.StackItem>
                    <Core.StackItem>
                      <FinalForm.Field name="debugFeature" type="checkbox">
                        {props => (
                          <Core.Switch
                            id="debug-feature-switch"
                            label="Debug Feature active"
                            labelOff="Debug Feature inactive"
                            isChecked={props.input.checked}
                            onChange={props.input.onChange}
                            ouiaId="DebugFeatureSwitch"
                          />
                        )}
                      </FinalForm.Field>
                      <FinalFormListeners.OnChange name="debugFeature">
                        {(value: boolean) => {
                          if (value === true) {
                            dispatch(activeDebug());
                          } else {
                            dispatch(inactiveDebug());
                          }
                        }}
                      </FinalFormListeners.OnChange>
                    </Core.StackItem>
                  </Core.Stack>
                </React.Fragment>
              )} />
          </Core.FormGroup>
        </Core.CardBody>
      </Core.Card>
    </React.Fragment>
  )
}