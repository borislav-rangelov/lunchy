import React from 'react';
import { Translate } from 'react-jhipster';
import { connect } from 'react-redux';
import { Row, Col, Alert } from 'reactstrap';
import { Link, RouteComponentProps } from 'react-router-dom';

import { IRootState } from 'app/shared/reducers';
import { getSession } from 'app/shared/reducers/authentication';
import { getMenuItemsForRestaurant } from 'app/entities/menu/menu.reducer';

export interface IPreviewRestaurantProp extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {
  menu: Array<{}>;
  loading: boolean;
}

export class PreviewRestaurant extends React.Component<IPreviewRestaurantProp> {
  constructor(props) {
    super(props);
    this.state = { menu: [], loading: true };
  }
  componentDidMount() {
    this.props.getMenuItemsForRestaurant(this.props.match.params.id).then(result => {
      this.setState({
        menu: result.value.data,
        loading: false
      });
    });
  }

  render() {
    const { loading } = this.state;
    if (loading) {
      return 'Loading';
    }
    const restaurant = this.state.menu.length && this.state.menu[0].restaurant.name;
    return (
      <Row>
        <Col>
          <h2>{restaurant} menus:</h2>

          {this.state.menu &&
            this.state.menu.map(menu => (
              <div key={menu.id}>
                <h5>{menu.name}</h5>
                <table className="table">
                  <thead>
                    <tr>
                      <th>Food</th>
                      <th>Grams</th>
                      <th>Price</th>
                    </tr>
                  </thead>
                  <tbody>
                    {menu.menuItems &&
                      menu.menuItems.map(item => (
                        <tr key={item.id}>
                          <td>{item.name}</td>
                          <td>{item.grams}</td>
                          <td>{item.price}</td>
                        </tr>
                      ))}
                  </tbody>
                </table>
              </div>
            ))}
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = storeState => ({});

const mapDispatchToProps = { getMenuItemsForRestaurant };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PreviewRestaurant);
