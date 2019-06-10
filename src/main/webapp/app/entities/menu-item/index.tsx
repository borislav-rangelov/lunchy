import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import MenuItem from './menu-item';
import MenuItemDetail from './menu-item-detail';
import MenuItemUpdate from './menu-item-update';
import MenuItemDeleteDialog from './menu-item-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MenuItemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MenuItemUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MenuItemDetail} />
      <ErrorBoundaryRoute path={match.url} component={MenuItem} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={MenuItemDeleteDialog} />
  </>
);

export default Routes;
