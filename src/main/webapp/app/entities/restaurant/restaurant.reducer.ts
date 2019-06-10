import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRestaurant, defaultValue } from 'app/shared/model/restaurant.model';

export const ACTION_TYPES = {
  FETCH_RESTAURANT_LIST: 'restaurant/FETCH_RESTAURANT_LIST',
  FETCH_RESTAURANT: 'restaurant/FETCH_RESTAURANT',
  CREATE_RESTAURANT: 'restaurant/CREATE_RESTAURANT',
  UPDATE_RESTAURANT: 'restaurant/UPDATE_RESTAURANT',
  DELETE_RESTAURANT: 'restaurant/DELETE_RESTAURANT',
  RESET: 'restaurant/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRestaurant>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type RestaurantState = Readonly<typeof initialState>;

// Reducer

export default (state: RestaurantState = initialState, action): RestaurantState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_RESTAURANT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_RESTAURANT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_RESTAURANT):
    case REQUEST(ACTION_TYPES.UPDATE_RESTAURANT):
    case REQUEST(ACTION_TYPES.DELETE_RESTAURANT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_RESTAURANT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_RESTAURANT):
    case FAILURE(ACTION_TYPES.CREATE_RESTAURANT):
    case FAILURE(ACTION_TYPES.UPDATE_RESTAURANT):
    case FAILURE(ACTION_TYPES.DELETE_RESTAURANT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_RESTAURANT_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links)
      };
    case SUCCESS(ACTION_TYPES.FETCH_RESTAURANT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_RESTAURANT):
    case SUCCESS(ACTION_TYPES.UPDATE_RESTAURANT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_RESTAURANT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/restaurants';

// Actions

export const getEntities: ICrudGetAllAction<IRestaurant> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_RESTAURANT_LIST,
    payload: axios.get<IRestaurant>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IRestaurant> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_RESTAURANT,
    payload: axios.get<IRestaurant>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IRestaurant> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_RESTAURANT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IRestaurant> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_RESTAURANT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRestaurant> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_RESTAURANT,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
