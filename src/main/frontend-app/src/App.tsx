import React from "react";
import "@patternfly/react-core/dist/styles/base.css";
import {
  Card, CardBody,
  Page, PageSection,
  Alert, FormAlert,
  Form, FormGroup, TextInput,
  Stack, StackItem, FormHelperText, HelperText, HelperTextItem
} from '@patternfly/react-core';
import { TableComposable, Thead, Tr, Th, Tbody, Td } from '@patternfly/react-table';

interface SampleItem {
  salesPerson: string,
  region: string,
  salesAmount: number,
  percentCommission: number,
  commissionAmount: number | null
};

const sample: SampleItem[] = [
  { salesPerson: 'Joe', region: 'North', salesAmount: 260, percentCommission: 10, commissionAmount: null },
  { salesPerson: 'Robert', region: 'South', salesAmount: 660, percentCommission: 15, commissionAmount: null },
  { salesPerson: 'Michelle', region: 'East', salesAmount: 940, percentCommission: 15, commissionAmount: null },
  { salesPerson: 'Erich', region: 'West', salesAmount: 410, percentCommission: 12, commissionAmount: null },
  { salesPerson: 'Dafna', region: 'North', salesAmount: 800, percentCommission: 15, commissionAmount: null },
  { salesPerson: 'Rob', region: 'South', salesAmount: 900, percentCommission: 15, commissionAmount: null }
];

const columnNames = {
  salesPerson: 'Sales Person',
  region: 'Region',
  salesAmount: 'Sales Amount',
  percentCommission: '% Commission',
  commissionAmount: 'Commission Amount'
};

function App() {
  return (
    <Page>
      <PageSection isWidthLimited isCenterAligned>
        <Card>
          <CardBody>
            <Stack hasGutter>
              <StackItem>
                <Form>
                  <FormGroup>
                    <TextInput
                    />
                    <FormHelperText isHidden={false} component="div">
                      <HelperText id="helper-text1">
                        <HelperTextItem variant={'default'}>Autosuggest</HelperTextItem>
                      </HelperText>
                    </FormHelperText>
                  </FormGroup>
                  <FormAlert>
                    <Alert variant="danger" title="Fill out all required fields before continuing." aria-live="polite" isInline />
                  </FormAlert>
                </Form>
              </StackItem>
              <StackItem isFilled>
                <TableComposable aria-label="Actions table">
                  <Thead>
                    <Tr>
                      <Th>{columnNames.salesPerson}</Th>
                      <Th>{columnNames.region}</Th>
                      <Th>{columnNames.salesAmount}</Th>
                      <Th>{columnNames.percentCommission}</Th>
                      <Th>{columnNames.commissionAmount}</Th>
                      <Td></Td>
                    </Tr>
                  </Thead>
                  <Tbody>
                    {sample.map(sampleItem => (
                      <Tr key={sampleItem.salesPerson}>
                        <Td dataLabel={columnNames.salesPerson}>{sampleItem.salesPerson}</Td>
                        <Td dataLabel={columnNames.region}>{sampleItem.region}</Td>
                        <Td dataLabel={columnNames.salesAmount}>{sampleItem.salesAmount}</Td>
                        <Td dataLabel={columnNames.percentCommission}>{sampleItem.percentCommission}</Td>
                        <Td dataLabel={columnNames.commissionAmount}>{sampleItem.commissionAmount}</Td>
                      </Tr>
                    ))}
                  </Tbody>
                </TableComposable>
              </StackItem>
            </Stack>
          </CardBody>
        </Card>
      </PageSection>
    </Page>
  );
};

export default App;
