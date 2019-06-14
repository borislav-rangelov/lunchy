import React from 'react';
import { connect } from 'react-redux';
import { Row, Col, Alert, Input } from 'reactstrap';
import { getEntities } from 'app/entities/restaurant/restaurant.reducer';

export interface ISearchRestaurantsProp extends StateProps, DispatchProps {}

export interface ISearchRestaurantsState {
  searchTerm: string;
}

export class SearchRestaurants extends React.Component<ISearchRestaurantsProp, ISearchRestaurantsState> {
  state = {
    searchTerm: ''
  };

  componentDidMount() {}

  onSearchTermChange = event => {
    this.setState({ searchTerm: event.target.value });
    this.props.getEntities();
  };

  render() {
    const { restaurants } = this.props;
    const { searchTerm } = this.state;
    const filteredRestaurants = restaurants.filter(r => r.name.toLowerCase().indexOf(searchTerm.toLowerCase()) > -1);
    return (
      <Row>
        <Col>
          <h2>Restaurants</h2>
          <Input value={searchTerm} onChange={this.onSearchTermChange} placeholder="Start searching for restaurants here..." />
          <ul>{filteredRestaurants && filteredRestaurants.map(restaurant => <li>{restaurant.name}</li>)}</ul>
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
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SearchRestaurants);
