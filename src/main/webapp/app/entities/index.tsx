import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Restaurant from './restaurant';
import RestaurantLocation from './restaurant-location';
import Menu from './menu';
import MenuItem from './menu-item';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/restaurant`} component={Restaurant} />
      <ErrorBoundaryRoute path={`${match.url}/restaurant-location`} component={RestaurantLocation} />
      <ErrorBoundaryRoute path={`${match.url}/menu`} component={Menu} />
      <ErrorBoundaryRoute path={`${match.url}/menu-item`} component={MenuItem} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
