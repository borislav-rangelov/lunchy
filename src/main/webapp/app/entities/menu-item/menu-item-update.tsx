import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IMenu } from 'app/shared/model/menu.model';
import { getEntities as getMenus } from 'app/entities/menu/menu.reducer';
import { getEntity, updateEntity, createEntity, reset } from './menu-item.reducer';
import { IMenuItem } from 'app/shared/model/menu-item.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IMenuItemUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IMenuItemUpdateState {
  isNew: boolean;
  menuId: string;
}

export class MenuItemUpdate extends React.Component<IMenuItemUpdateProps, IMenuItemUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      menuId: '0',
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

    this.props.getMenus();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { menuItemEntity } = this.props;
      const entity = {
        ...menuItemEntity,
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
    this.props.history.push('/entity/menu-item');
  };

  render() {
    const { menuItemEntity, menus, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="lunchyApp.menuItem.home.createOrEditLabel">
              <Translate contentKey="lunchyApp.menuItem.home.createOrEditLabel">Create or edit a MenuItem</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : menuItemEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="menu-item-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="menu-item-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="menu-item-name">
                    <Translate contentKey="lunchyApp.menuItem.name">Name</Translate>
                  </Label>
                  <AvField
                    id="menu-item-name"
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
                  <Label id="descriptionLabel" for="menu-item-description">
                    <Translate contentKey="lunchyApp.menuItem.description">Description</Translate>
                  </Label>
                  <AvField
                    id="menu-item-description"
                    type="text"
                    name="description"
                    validate={{
                      maxLength: { value: 6000, errorMessage: translate('entity.validation.maxlength', { max: 6000 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="priceLabel" for="menu-item-price">
                    <Translate contentKey="lunchyApp.menuItem.price">Price</Translate>
                  </Label>
                  <AvField
                    id="menu-item-price"
                    type="string"
                    className="form-control"
                    name="price"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      min: { value: 0, errorMessage: translate('entity.validation.min', { min: 0 }) },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="gramsLabel" for="menu-item-grams">
                    <Translate contentKey="lunchyApp.menuItem.grams">Grams</Translate>
                  </Label>
                  <AvField
                    id="menu-item-grams"
                    type="string"
                    className="form-control"
                    name="grams"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      min: { value: 0, errorMessage: translate('entity.validation.min', { min: 0 }) },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="menu-item-menu">
                    <Translate contentKey="lunchyApp.menuItem.menu">Menu</Translate>
                  </Label>
                  <AvInput
                    id="menu-item-menu"
                    type="select"
                    className="form-control"
                    name="menu.id"
                    value={isNew ? menus[0] && menus[0].id : menuItemEntity.menu.id}
                    required
                  >
                    {menus
                      ? menus.map(otherEntity => (
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
                <Button tag={Link} id="cancel-save" to="/entity/menu-item" replace color="info">
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
  menus: storeState.menu.entities,
  menuItemEntity: storeState.menuItem.entity,
  loading: storeState.menuItem.loading,
  updating: storeState.menuItem.updating,
  updateSuccess: storeState.menuItem.updateSuccess
});

const mapDispatchToProps = {
  getMenus,
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
)(MenuItemUpdate);
