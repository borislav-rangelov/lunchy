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
import { getEntity, updateEntity, createEntity, reset } from './menu.reducer';
import { IMenu } from 'app/shared/model/menu.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IMenuUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IMenuUpdateState {
  isNew: boolean;
  restaurantId: string;
}

export class MenuUpdate extends React.Component<IMenuUpdateProps, IMenuUpdateState> {
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
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getRestaurants();
  }

  saveEntity = (event, errors, values) => {
    values.validFrom = convertDateTimeToServer(values.validFrom);
    values.validTo = convertDateTimeToServer(values.validTo);

    if (errors.length === 0) {
      const { menuEntity } = this.props;
      const entity = {
        ...menuEntity,
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
    this.props.history.push('/entity/menu');
  };

  render() {
    const { menuEntity, restaurants, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="lunchyApp.menu.home.createOrEditLabel">
              <Translate contentKey="lunchyApp.menu.home.createOrEditLabel">Create or edit a Menu</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : menuEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="menu-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="menu-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="menu-name">
                    <Translate contentKey="lunchyApp.menu.name">Name</Translate>
                  </Label>
                  <AvField
                    id="menu-name"
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
                  <Label id="validFromLabel" for="menu-validFrom">
                    <Translate contentKey="lunchyApp.menu.validFrom">Valid From</Translate>
                  </Label>
                  <AvInput
                    id="menu-validFrom"
                    type="datetime-local"
                    className="form-control"
                    name="validFrom"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.menuEntity.validFrom)}
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="validToLabel" for="menu-validTo">
                    <Translate contentKey="lunchyApp.menu.validTo">Valid To</Translate>
                  </Label>
                  <AvInput
                    id="menu-validTo"
                    type="datetime-local"
                    className="form-control"
                    name="validTo"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.menuEntity.validTo)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="menu-restaurant">
                    <Translate contentKey="lunchyApp.menu.restaurant">Restaurant</Translate>
                  </Label>
                  <AvInput
                    id="menu-restaurant"
                    type="select"
                    className="form-control"
                    name="restaurant.id"
                    value={isNew ? restaurants[0] && restaurants[0].id : menuEntity.restaurant.id}
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
                <Button tag={Link} id="cancel-save" to="/entity/menu" replace color="info">
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
  menuEntity: storeState.menu.entity,
  loading: storeState.menu.loading,
  updating: storeState.menu.updating,
  updateSuccess: storeState.menu.updateSuccess
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
)(MenuUpdate);
