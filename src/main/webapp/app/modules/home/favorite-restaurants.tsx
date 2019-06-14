import React from 'react';
import { connect } from 'react-redux';
import { Row, Col, Alert, Card, CardImg, CardText, CardBody, CardTitle, CardSubtitle, Button } from 'reactstrap';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from 'app/entities/restaurant/restaurant.reducer';
import { withRouter, RouteComponentProps } from 'react-router';

export interface IFavoriteRestaurantsProp extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class FavoriteRestaurants extends React.Component<IFavoriteRestaurantsProp> {
  componentDidMount() {
    this.props.getEntities();
  }

  onRestaurantClick = (event): void => {
    this.props.history.push(`/preview-restaurant/${event.target.dataset.id}`);
  };

  render() {
    const { favoriteRestaurants } = this.props;
    return (
      <Row>
        {favoriteRestaurants.map(fr => (
          <Col md="3" key={fr.id} data-id={fr.id} onClick={this.onRestaurantClick}>
            <Card>
              <CardBody>
                <CardTitle>{fr.name}</CardTitle>
              </CardBody>
            </Card>
          </Col>
        ))}
      </Row>
    );
  }
}

const mapStateToProps = storeState => ({
  account: storeState.authentication.account,
  isAuthenticated: storeState.authentication.isAuthenticated,
  favoriteRestaurants: storeState.restaurant.entities.slice(0, 4)
});

const mapDispatchToProps = { getEntities };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default withRouter(
  connect(
    mapStateToProps,
    mapDispatchToProps
  )(FavoriteRestaurants)
);
