import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './restaurant-location.reducer';
import { IRestaurantLocation } from 'app/shared/model/restaurant-location.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRestaurantLocationDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class RestaurantLocationDetail extends React.Component<IRestaurantLocationDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { restaurantLocationEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="lunchyApp.restaurantLocation.detail.title">RestaurantLocation</Translate> [
            <b>{restaurantLocationEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="lunchyApp.restaurantLocation.name">Name</Translate>
              </span>
            </dt>
            <dd>{restaurantLocationEntity.name}</dd>
            <dt>
              <span id="locationString">
                <Translate contentKey="lunchyApp.restaurantLocation.locationString">Location String</Translate>
              </span>
            </dt>
            <dd>{restaurantLocationEntity.locationString}</dd>
            <dt>
              <Translate contentKey="lunchyApp.restaurantLocation.restaurant">Restaurant</Translate>
            </dt>
            <dd>{restaurantLocationEntity.restaurant ? restaurantLocationEntity.restaurant.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/restaurant-location" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/restaurant-location/${restaurantLocationEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ restaurantLocation }: IRootState) => ({
  restaurantLocationEntity: restaurantLocation.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RestaurantLocationDetail);
