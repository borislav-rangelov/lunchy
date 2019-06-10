import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IRestaurant } from 'app/shared/model/restaurant.model';
import { getEntities as getRestaurants } from 'app/entities/restaurant/restaurant.reducer';
import { getEntity, updateEntity, createEntity, reset } from './restaurant-location.reducer';
import { IRestaurantLocation } from 'app/shared/model/restaurant-location.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IRestaurantLocationUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IRestaurantLocationUpdateState {
  isNew: boolean;
  restaurantId: string;
}

export class RestaurantLocationUpdate extends React.Component<IRestaurantLocationUpdateProps, IRestaurantLocationUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      restaurantId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (!this.state.isNew) {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getRestaurants();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { restaurantLocationEntity } = this.props;
      const entity = {
        ...restaurantLocationEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/restaurant-location');
  };

  render() {
    const { restaurantLocationEntity, restaurants, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="lunchyApp.restaurantLocation.home.createOrEditLabel">
              <Translate contentKey="lunchyApp.restaurantLocation.home.createOrEditLabel">Create or edit a RestaurantLocation</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : restaurantLocationEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="restaurant-location-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="restaurant-location-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="restaurant-location-name">
                    <Translate contentKey="lunchyApp.restaurantLocation.name">Name</Translate>
                  </Label>
                  <AvField
                    id="restaurant-location-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      minLength: { value: 3, errorMessage: translate('entity.validation.minlength', { min: 3 }) },
                      maxLength: { value: 50, errorMessage: translate('entity.validation.maxlength', { max: 50 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="locationStringLabel" for="restaurant-location-locationString">
                    <Translate contentKey="lunchyApp.restaurantLocation.locationString">Location String</Translate>
                  </Label>
                  <AvField
                    id="restaurant-location-locationString"
                    type="text"
                    name="locationString"
                    validate={{
                      maxLength: { value: 2500, errorMessage: translate('entity.validation.maxlength', { max: 2500 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="restaurant-location-restaurant">
                    <Translate contentKey="lunchyApp.restaurantLocation.restaurant">Restaurant</Translate>
                  </Label>
                  <AvInput
                    id="restaurant-location-restaurant"
                    type="select"
                    className="form-control"
                    name="restaurant.id"
                    value={isNew ? restaurants[0] && restaurants[0].id : restaurantLocationEntity.restaurant.id}
                    required
                  >
                    {restaurants
                      ? restaurants.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                  <AvFeedback>
                    <Translate contentKey="entity.validation.required">This field is required.</Translate>
                  </AvFeedback>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/restaurant-location" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  restaurants: storeState.restaurant.entities,
  restaurantLocationEntity: storeState.restaurantLocation.entity,
  loading: storeState.restaurantLocation.loading,
  updating: storeState.restaurantLocation.updating,
  updateSuccess: storeState.restaurantLocation.updateSuccess
});

const mapDispatchToProps = {
  getRestaurants,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RestaurantLocationUpdate);
