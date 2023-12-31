import React from 'react';

import './highlight.css';
import { useAppSelector, useAppDispatch } from '../../app/hooks';
import * as Core from '@patternfly/react-core';
import * as Table from '@patternfly/react-table';
import * as Icon from '@patternfly/react-icons';
import { selectFormula } from '../formula/formulaSlice';
import { ElementExecutionState, InputState, selectSamples } from '../samples/samplesSlice';
import { selectExecutionsDebug, selectExecutionsDebugSelected, selectSalesPerson } from './executionsDebugSlice';
import HighlightWithinTextarea from 'react-highlight-within-textarea';

export function ExecutionsDebug() {
  const formula = useAppSelector(selectFormula);
  const samples = useAppSelector(selectSamples);
  const executionsDebug = useAppSelector(selectExecutionsDebug);
  const executionsDebugSelected = useAppSelector(selectExecutionsDebugSelected);
  const dispatch = useAppDispatch();
  const names = {
    executedAtStart: 'Executed At Start',
    executedAtEnd: 'Executed At End',
    inputs: 'Inputs',
    result: 'Result',
    underline: 'Underline',
    processedInMillis: 'Processed (in millis)'
  };

  const result: string | null = executionsDebugSelected.length > 0 ? executionsDebugSelected[0].result : null;
  const processedInMillis: number | null = executionsDebugSelected.length > 0 ? executionsDebugSelected[0].processedInNanos / 1000000 : null;
  const executedAtStart: string | null = executionsDebugSelected.length > 0 ? executionsDebugSelected[0].executedAtStart : null;
  const executedAtEnd: string | null = executionsDebugSelected.length > 0 ? executionsDebugSelected[0].executedAtEnd : null;
  return (
    <React.Fragment>
      <Core.Card>
        <Core.CardHeader>
          <Core.ToggleGroup>
            {samples.samples.map(sample => {
              return (
                <Core.ToggleGroupItem
                  key={sample.salesPerson}
                  text={sample.salesPerson}
                  isSelected={sample.salesPerson === executionsDebug.selectedSalesPerson}
                  onChange={() => dispatch(selectSalesPerson(sample.salesPerson))}
                />)
            })}
          </Core.ToggleGroup>
        </Core.CardHeader>
        <Core.CardBody>
          <Core.Sidebar hasBorder>
            <Core.SidebarPanel width={{ default: 'width_25' }}>
              <Core.DescriptionList>
                <Core.DescriptionListGroup>
                  <Core.DescriptionListTerm>{names.result}</Core.DescriptionListTerm>
                  <Core.DescriptionListDescription>{result}</Core.DescriptionListDescription>
                </Core.DescriptionListGroup>
                <Core.DescriptionListGroup>
                  <Core.DescriptionListTerm>{names.executedAtStart}</Core.DescriptionListTerm>
                  <Core.DescriptionListDescription>{executedAtStart}</Core.DescriptionListDescription>
                </Core.DescriptionListGroup>
                <Core.DescriptionListGroup>
                  <Core.DescriptionListTerm>{names.executedAtEnd}</Core.DescriptionListTerm>
                  <Core.DescriptionListDescription>{executedAtEnd}</Core.DescriptionListDescription>
                </Core.DescriptionListGroup>
                <Core.DescriptionListGroup>
                  <Core.DescriptionListTerm>{names.processedInMillis}</Core.DescriptionListTerm>
                  <Core.DescriptionListDescription>{processedInMillis !== null &&
                    `${processedInMillis} milliseconds`}</Core.DescriptionListDescription>
                </Core.DescriptionListGroup>
              </Core.DescriptionList>
            </Core.SidebarPanel>
            <Core.SidebarContent>
              <Table.Table variant='compact'>
                <Table.Thead>
                  <Table.Tr>
                    <Table.Th width={40}>{names.underline}</Table.Th>
                    <Table.Th width={15}>{names.inputs}</Table.Th>
                    <Table.Th width={10}>{names.result}</Table.Th>
                    <Table.Th width={15}>{names.processedInMillis}</Table.Th>
                  </Table.Tr>
                </Table.Thead>
                <Table.Tbody>
                  {executionsDebugSelected
                    .flatMap(executionsResult => executionsResult.elementExecutions)
                    .map((elementExecution: ElementExecutionState, index: number) => {
                      const highlight = [
                        {
                          highlight: [elementExecution.range.start, elementExecution.range.end + 1],
                          className: "formula"
                        }
                      ];
                      return (
                        <Table.Tr key={index}>
                          <Table.Td dataLabel={names.underline}>
                            <HighlightWithinTextarea
                              value={formula.formula}
                              highlight={highlight}
                            />
                          </Table.Td>
                          <Table.Td dataLabel={names.inputs}>
                            <Core.List isPlain isBordered>
                              {elementExecution.inputs.map((input: InputState) => (
                                <Core.ListItem key={input.name}>
                                  {
                                    <span>{input.name}: {input.value} <b>{formula.formula.substring(input.range.start, input.range.end + 1)}</b></span>
                                  }
                                </Core.ListItem>
                              ))}
                            </Core.List>
                          </Table.Td>
                          <Table.Td dataLabel={names.result}>{elementExecution.result}</Table.Td>
                          <Table.Td dataLabel={names.processedInMillis}>
                            <Core.Tooltip aria="none" aria-live="polite" content={
                              <React.Fragment>
                                <Icon.OutlinedClockIcon /> {elementExecution.executedAtStart} <br></br>
                                <Icon.OutlinedClockIcon /> {elementExecution.executedAtEnd}
                              </React.Fragment>
                            }>
                              <React.Fragment>
                                <Icon.OutlinedClockIcon /> {processedInMillis !== null &&
                                  `${processedInMillis} milliseconds`}
                              </React.Fragment>
                            </Core.Tooltip>
                          </Table.Td>
                        </Table.Tr>
                      )
                    })}
                </Table.Tbody>
              </Table.Table>
            </Core.SidebarContent>
          </Core.Sidebar>
        </Core.CardBody>
      </Core.Card >
    </React.Fragment >
  );
}