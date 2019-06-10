import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RestaurantLocation from './restaurant-location';
import RestaurantLocationDetail from './restaurant-location-detail';
import RestaurantLocationUpdate from './restaurant-location-update';
import RestaurantLocationDeleteDialog from './restaurant-location-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RestaurantLocationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RestaurantLocationUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RestaurantLocationDetail} />
      <ErrorBoundaryRoute path={match.url} component={RestaurantLocation} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={RestaurantLocationDeleteDialog} />
  </>
);

export default Routes;
