import React from 'react';

import './highlight.css';
import { useAppSelector, useAppDispatch } from '../../app/hooks';
import * as Core from '@patternfly/react-core';
import * as Table from '@patternfly/react-table';
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
    exactProcessedInMillis: 'Exact processed (in millis)',
    parserExecutionProcessedInMillis: 'Exact parser processed (in millis)'
  };

  const result: string | null = executionsDebugSelected?.executionsResult.result || null;
  const exactProcessedInNanos: number | null = executionsDebugSelected?.executionsResult.exactProcessedInNanos! / 1000000 || null;
  const executedAtStart: string | null = executionsDebugSelected?.executionsResult.executedAtStart || null;
  const executedAtEnd: string | null = executionsDebugSelected?.executionsResult.executedAtEnd || null;
  const parserExecutionProcessedInNanos: number | null = executionsDebugSelected?.executionsResult?.parserExecutionProcessedIn?.processedInNanos! / 1000000 || null;
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
                  <Core.DescriptionListTermHelpText>
                    <Core.Popover bodyContent={<>sum of parser processed and executions</>}>
                      <Core.DescriptionListTermHelpTextButton>{names.exactProcessedInMillis}</Core.DescriptionListTermHelpTextButton>
                    </Core.Popover>
                  </Core.DescriptionListTermHelpText>
                  <Core.DescriptionListDescription>{exactProcessedInNanos !== null &&
                    `${exactProcessedInNanos} milliseconds`}</Core.DescriptionListDescription>
                </Core.DescriptionListGroup>
                <Core.Divider />
                <Core.DescriptionListGroup>
                  <Core.DescriptionListTerm>{names.parserExecutionProcessedInMillis}</Core.DescriptionListTerm>
                  <Core.DescriptionListDescription>{parserExecutionProcessedInNanos !== null &&
                    `${parserExecutionProcessedInNanos} milliseconds`}</Core.DescriptionListDescription>
                  <Core.DescriptionListDescription>{parserExecutionProcessedInNanos === null && exactProcessedInNanos !== null &&
                    'Retreived from cache'}</Core.DescriptionListDescription>
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
                    <Table.Th width={15}>{names.exactProcessedInMillis}</Table.Th>
                  </Table.Tr>
                </Table.Thead>
                <Table.Tbody>
                  {executionsDebugSelected?.executionsResult.elementExecutions
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
                          <Table.Td dataLabel={names.exactProcessedInMillis}>
                            {elementExecution.processedInNanos / 1000000} milliseconds
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