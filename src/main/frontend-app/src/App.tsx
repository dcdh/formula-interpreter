import React from 'react';
import '@patternfly/react-core/dist/styles/base.css';
import * as Core from '@patternfly/react-core';
import { Formula } from './features/formula/Formula';
import { Samples } from './features/samples/Samples';
import { ExecutionsDebug } from './features/executionsDebug/ExecutionsDebug';

function App() {
  const [alerts, setAlerts] = React.useState<Partial<Core.AlertProps>[]>([]);

  const getUniqueId = () => new Date().getTime();

  const addAlert = (title: string, variant: Core.AlertProps['variant']) => {
    const key: React.Key = getUniqueId();
    setAlerts(prevAlerts => [...prevAlerts, { title, variant, key }]);
  };

  const removeAlert = (key: React.Key) => {
    setAlerts(prevAlerts => [...prevAlerts.filter(alert => alert.key !== key)]);
  };

  return (
    <React.Fragment>
      <Core.Page>
        <Core.PageSection isWidthLimited isCenterAligned>
          <Core.Grid hasGutter>
            <Core.GridItem span={12}><Formula addAlert={addAlert} /></Core.GridItem>
            <Core.GridItem span={12}><Samples /></Core.GridItem>
            <Core.GridItem span={12}><ExecutionsDebug /></Core.GridItem>
          </Core.Grid>
        </Core.PageSection>
      </Core.Page>
      <Core.AlertGroup isToast isLiveRegion>
        {alerts.map(({ key, variant, title }) => (
          <Core.Alert
            variant={Core.AlertVariant[variant!]}
            title={title}
            timeout={5000}
            onTimeout={() => removeAlert(key!)}
            isLiveRegion
            actionClose={
              <Core.AlertActionCloseButton
                variantLabel={`${variant} alert`} onClose={() => removeAlert(key!)} />
            }
            key={key}
          />
        ))}
      </Core.AlertGroup>
    </React.Fragment>
  );
};

export default App;
