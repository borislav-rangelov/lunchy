import { IMenu } from 'app/shared/model/menu.model';

export interface IMenuItem {
  id?: number;
  name?: string;
  description?: string;
  price?: number;
  grams?: number;
  menu?: IMenu;
}

export const defaultValue: Readonly<IMenuItem> = {};
