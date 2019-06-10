import { IRestaurant } from 'app/shared/model/restaurant.model';

export interface IRestaurantLocation {
  id?: number;
  name?: string;
  locationString?: string;
  restaurant?: IRestaurant;
}

export const defaultValue: Readonly<IRestaurantLocation> = {};
