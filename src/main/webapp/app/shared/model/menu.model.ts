import { Moment } from 'moment';
import { IMenuItem } from 'app/shared/model/menu-item.model';
import { IRestaurant } from 'app/shared/model/restaurant.model';

export interface IMenu {
  id?: number;
  name?: string;
  validFrom?: Moment;
  validTo?: Moment;
  menuItems?: IMenuItem[];
  restaurant?: IRestaurant;
}

export const defaultValue: Readonly<IMenu> = {};
