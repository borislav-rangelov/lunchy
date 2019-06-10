import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Translate, translate } from 'react-jhipster';
import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  // tslint:disable-next-line:jsx-self-close
  <NavDropdown icon="th-list" name={translate('global.menu.entities.main')} id="entity-menu">
    <MenuItem icon="asterisk" to="/entity/restaurant">
      <Translate contentKey="global.menu.entities.restaurant" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/restaurant-location">
      <Translate contentKey="global.menu.entities.restaurantLocation" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/menu">
      <Translate contentKey="global.menu.entities.menu" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/menu-item">
      <Translate contentKey="global.menu.entities.menuItem" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
