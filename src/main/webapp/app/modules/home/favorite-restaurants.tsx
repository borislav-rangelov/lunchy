import React from 'react';
import { connect } from 'react-redux';
import { Row, Col, Alert, Card, CardImg, CardText, CardBody, CardTitle, CardSubtitle, Button } from 'reactstrap';

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
      <Row>
        {favoriteRestaurants.map(fr => (
          <Col md="3" key={fr.id}>
            <Card>
              <CardImg top width="100%" src=" http://placekitten.com/300/200" alt="Card image cap" />
              <CardBody>
                <CardTitle>{fr.name}</CardTitle>
                <CardSubtitle>Card subtitle</CardSubtitle>
                <CardText>Some quick example text to build on the card title and make up the bulk of the card's content.</CardText>
                <Button>Button</Button>
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

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(FavoriteRestaurants);
