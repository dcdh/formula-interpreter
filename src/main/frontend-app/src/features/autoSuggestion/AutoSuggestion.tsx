import { useAppSelector } from '../../app/hooks';
import * as Core from '@patternfly/react-core';
import { selectTokens } from './autoSuggestionSlice';
import React from 'react';

export function AutoSuggestion() {
  const tokens = useAppSelector(selectTokens);

  return (
    <React.Fragment>{
      tokens.length > 0 &&
      <Core.MenuList>
        {
          tokens.map(token => {
            return <Core.MenuItem key={token}>{token}</Core.MenuItem>
          })
        }
      </Core.MenuList>}
    </React.Fragment>
  );
}