import React from 'react';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { Row, Col, Alert } from 'reactstrap';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from 'app/entities/restaurant/restaurant.reducer';

export interface IFavoriteRestaurantsProp extends StateProps, DispatchProps {}

export class FavoriteRestaurants extends React.Component<IFavoriteRestaurantsProp> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { favoriteRestaurants } = this.props;
    return (
      <ul>
        {favoriteRestaurants.map(fr => (
          <li>{fr.name}</li>
        ))}
      </ul>
    );
  }
}

const mapStateToProps = storeState => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated,
  favoriteRestaurants: storeState.restaurant.entities
});

const mapDispatchToProps = { getEntities };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(FavoriteRestaurants);
