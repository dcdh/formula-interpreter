import React from 'react';

import { useAppSelector, useAppDispatch } from '../../app/hooks';
import * as Core from '@patternfly/react-core';
import * as Table from '@patternfly/react-table';
import * as Icon from '@patternfly/react-icons';
import { selectFormula } from '../formula/formulaSlice';
import { ElementExecutionState, selectSamples } from '../samples/samplesSlice';
import { selectExecutionsDebug, selectExecutionsDebugSelected, selectSalesPerson } from './executionsDebugSlice';

export function ExecutionsDebug() {
  const formula = useAppSelector(selectFormula);
  const samples = useAppSelector(selectSamples);
  const executionsDebug = useAppSelector(selectExecutionsDebug);
  const executionsDebugSelected = useAppSelector(selectExecutionsDebugSelected);
  const dispatch = useAppDispatch();
  const columnNames = {
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
          <Core.Stack>
            <Core.StackItem>
              <Core.Flex spaceItems={{ default: 'spaceItemsMd' }}>
                <Core.FlexItem>
                  <Icon.CheckCircleIcon color='green' /> {result}
                </Core.FlexItem>
                <Core.FlexItem>
                  <Icon.OutlinedClockIcon /> {executedAtStart}
                </Core.FlexItem>
                <Core.FlexItem>
                  <Icon.OutlinedClockIcon /> {executedAtEnd}
                </Core.FlexItem>
                <Core.FlexItem>
                  <Icon.OutlinedClockIcon /> {processedInMillis !== null &&
                    `${processedInMillis} milliseconds`}
                </Core.FlexItem>
              </Core.Flex>
            </Core.StackItem>
            <Core.StackItem>
              <Table.Table variant='compact'>
                <Table.Thead>
                  <Table.Tr>
                    <Table.Td>{columnNames.underline}</Table.Td>
                    <Table.Td>{columnNames.inputs}</Table.Td>
                    <Table.Td>{columnNames.result}</Table.Td>
                    <Table.Td>{columnNames.executedAtStart}</Table.Td>
                    <Table.Td>{columnNames.executedAtEnd}</Table.Td>
                    <Table.Td>{columnNames.processedInMillis}</Table.Td>
                  </Table.Tr>
                </Table.Thead>
                <Table.Tbody>
                  {executionsDebugSelected
                    .flatMap(executionsResult => executionsResult.elementExecutions)
                    .map((elementExecution: ElementExecutionState, index: number) => {
                      return (
                        <Table.Tr key={index}>
                          <Table.Td dataLabel={columnNames.underline}>
                            {
                              formula.formula.substring(0, elementExecution.position!.start!)
                            }
                            <b>
                              {
                                formula.formula.substring(elementExecution.position!.start!, elementExecution.position!.end! + 1)
                              }
                            </b>
                            {
                              formula.formula.substring(elementExecution.position!.end! + 1, formula.formula.length)
                            }
                          </Table.Td>
                          <Table.Td dataLabel={columnNames.inputs}>
                            <Core.List isPlain isBordered>
                              {
                                Object.entries(elementExecution.inputs!)
                                  .map(([key, value]) => {
                                    return (
                                      <Core.ListItem key={key}>{key}: {value}</Core.ListItem>
                                    )
                                  })
                              }
                            </Core.List>
                          </Table.Td>
                          <Table.Td dataLabel={columnNames.result}>{elementExecution.result}</Table.Td>
                          <Table.Td dataLabel={columnNames.executedAtStart}>{elementExecution.executedAtStart}</Table.Td>
                          <Table.Td dataLabel={columnNames.executedAtEnd}>{elementExecution.executedAtEnd}</Table.Td>
                          <Table.Td dataLabel={columnNames.processedInMillis}>{elementExecution.processedInNanos / 1000000}</Table.Td>
                        </Table.Tr>
                      )
                    })}
                </Table.Tbody>
              </Table.Table>
            </Core.StackItem>
          </Core.Stack>
        </Core.CardBody>
      </Core.Card>
    </React.Fragment>
  );
}