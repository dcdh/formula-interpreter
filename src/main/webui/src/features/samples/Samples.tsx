import { useAppSelector } from '../../app/hooks';
import * as Core from '@patternfly/react-core';
import * as Table from '@patternfly/react-table';
import { selectSamples } from './samplesSlice';

export function Samples() {
  const samples = useAppSelector(selectSamples);
  const columnNames = {
    salesPerson: 'Sales Person',
    region: 'Region',
    salesAmount: 'Sales Amount',
    percentCommission: '% Commission',
    commissionAmount: 'Commission Amount'
  };

  return (
    <Core.Card>
      <Core.CardBody>
        <Table.Table variant='compact'>
          <Table.Thead>
            <Table.Tr>
              <Table.Th width={15}>{columnNames.salesPerson}</Table.Th>
              <Table.Th width={15}>{columnNames.region}</Table.Th>
              <Table.Th width={15}>{columnNames.salesAmount}</Table.Th>
              <Table.Th width={15}>{columnNames.percentCommission}</Table.Th>
              <Table.Th width={15}>{columnNames.commissionAmount}</Table.Th>
              <Table.Td width={25}></Table.Td>
            </Table.Tr>
          </Table.Thead>
          <Table.Tbody>
            {samples.samples.map(sample => (
              <Table.Tr key={sample.salesPerson}>
                <Table.Td dataLabel={columnNames.salesPerson}>{sample.salesPerson}</Table.Td>
                <Table.Td dataLabel={columnNames.region}>{sample.region}</Table.Td>
                <Table.Td dataLabel={columnNames.salesAmount}>{sample.salesAmount}</Table.Td>
                <Table.Td dataLabel={columnNames.percentCommission}>{sample.percentCommission}</Table.Td>
                <Table.Td dataLabel={columnNames.commissionAmount}>
                  {sample.status === 'notExecutedYet' &&
                    <Core.Label color="blue">Not executed yet</Core.Label>
                  }
                  {sample.status === 'executed' &&
                    <Core.Label color="green">{sample.commissionAmount}</Core.Label>
                  }
                  {sample.status === 'processing' &&
                    <Core.Spinner size="sm" />
                  }
                  {sample.status === 'failed' &&
                    <Core.Label color="red">Somethings wrong happened</Core.Label>
                  }
                  {sample.status === 'formulaInError' &&
                    <Core.Label color="red">Formula in error unable to process</Core.Label>
                  }
                  {sample.status === 'formulaInvalid' &&
                    <Core.Label color="orange">Formula invalid</Core.Label>
                  }
                </Table.Td>
              </Table.Tr>
            ))}
          </Table.Tbody>
        </Table.Table>
      </Core.CardBody>
    </Core.Card>
  )
}