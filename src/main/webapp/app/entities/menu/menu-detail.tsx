import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './menu.reducer';
import { IMenu } from 'app/shared/model/menu.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMenuDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class MenuDetail extends React.Component<IMenuDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { menuEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="lunchyApp.menu.detail.title">Menu</Translate> [<b>{menuEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="lunchyApp.menu.name">Name</Translate>
              </span>
            </dt>
            <dd>{menuEntity.name}</dd>
            <dt>
              <span id="validFrom">
                <Translate contentKey="lunchyApp.menu.validFrom">Valid From</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={menuEntity.validFrom} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="validTo">
                <Translate contentKey="lunchyApp.menu.validTo">Valid To</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={menuEntity.validTo} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <Translate contentKey="lunchyApp.menu.restaurant">Restaurant</Translate>
            </dt>
            <dd>{menuEntity.restaurant ? menuEntity.restaurant.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/menu" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/menu/${menuEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ menu }: IRootState) => ({
  menuEntity: menu.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MenuDetail);
