import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Restaurant from './restaurant';
import RestaurantDetail from './restaurant-detail';
import RestaurantUpdate from './restaurant-update';
import RestaurantDeleteDialog from './restaurant-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RestaurantUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RestaurantUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RestaurantDetail} />
      <ErrorBoundaryRoute path={match.url} component={Restaurant} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={RestaurantDeleteDialog} />
  </>
);

export default Routes;
