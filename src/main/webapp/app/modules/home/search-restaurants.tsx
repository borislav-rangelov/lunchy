import React from 'react';
import { connect } from 'react-redux';
import { Row, Col, Alert, Input } from 'reactstrap';
import { getEntitiesByName } from 'app/entities/restaurant/restaurant.reducer';
import { Link, RouteComponentProps, withRouter } from 'react-router-dom';

import { debounce } from 'lodash';

export interface ISearchRestaurantsProp extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ISearchRestaurantsState {
  searchTerm: string;
}

export class SearchRestaurants extends React.Component<ISearchRestaurantsProp, ISearchRestaurantsState> {
  state = {
    searchTerm: ''
  };

  searchForRestaurants = debounce(() => {
    this.props.getEntitiesByName(this.state.searchTerm);
  }, 300);

  onRestaurantClick = (event): void => {
    this.props.history.push(`/preview-restaurant/${event.target.dataset.id}`);
  };

  onSearchTermChange = event => {
    this.setState({ searchTerm: event.target.value }, this.searchForRestaurants);
  };

  render() {
    const { restaurants } = this.props;
    const { searchTerm } = this.state;
    // const filteredRestaurants = restaurants.filter(r => r.name.toLowerCase().indexOf(searchTerm.toLowerCase()) > -1);
    return (
      <Row>
        <Col>
          <h2>Restaurants</h2>
          <Input value={searchTerm} onChange={this.onSearchTermChange} placeholder="Start searching for restaurants here..." />
          <ul>
            {restaurants &&
              restaurants.map(restaurant => (
                <li key={restaurant.id} data-id={restaurant.id} onClick={this.onRestaurantClick}>
                  {restaurant.name}
                </li>
              ))}
          </ul>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = storeState => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated,
  restaurants: storeState.restaurant.entities
});

const mapDispatchToProps = {
  getEntitiesByName
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default withRouter(
  connect(
    mapStateToProps,
    mapDispatchToProps
  )(SearchRestaurants)
);
