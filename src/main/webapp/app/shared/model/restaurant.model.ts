import { IRestaurantLocation } from 'app/shared/model/restaurant-location.model';
import { IMenu } from 'app/shared/model/menu.model';

export interface IRestaurant {
  id?: number;
  name?: string;
  restaurantLocations?: IRestaurantLocation[];
  menus?: IMenu[];
}

export const defaultValue: Readonly<IRestaurant> = {};
