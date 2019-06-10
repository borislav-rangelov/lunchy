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

import { IRestaurantLocation, defaultValue } from 'app/shared/model/restaurant-location.model';

export const ACTION_TYPES = {
  FETCH_RESTAURANTLOCATION_LIST: 'restaurantLocation/FETCH_RESTAURANTLOCATION_LIST',
  FETCH_RESTAURANTLOCATION: 'restaurantLocation/FETCH_RESTAURANTLOCATION',
  CREATE_RESTAURANTLOCATION: 'restaurantLocation/CREATE_RESTAURANTLOCATION',
  UPDATE_RESTAURANTLOCATION: 'restaurantLocation/UPDATE_RESTAURANTLOCATION',
  DELETE_RESTAURANTLOCATION: 'restaurantLocation/DELETE_RESTAURANTLOCATION',
  RESET: 'restaurantLocation/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRestaurantLocation>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type RestaurantLocationState = Readonly<typeof initialState>;

// Reducer

export default (state: RestaurantLocationState = initialState, action): RestaurantLocationState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_RESTAURANTLOCATION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_RESTAURANTLOCATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_RESTAURANTLOCATION):
    case REQUEST(ACTION_TYPES.UPDATE_RESTAURANTLOCATION):
    case REQUEST(ACTION_TYPES.DELETE_RESTAURANTLOCATION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_RESTAURANTLOCATION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_RESTAURANTLOCATION):
    case FAILURE(ACTION_TYPES.CREATE_RESTAURANTLOCATION):
    case FAILURE(ACTION_TYPES.UPDATE_RESTAURANTLOCATION):
    case FAILURE(ACTION_TYPES.DELETE_RESTAURANTLOCATION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_RESTAURANTLOCATION_LIST):
      const links = parseHeaderForLinks(action.payload.headers.link);
      return {
        ...state,
        links,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links)
      };
    case SUCCESS(ACTION_TYPES.FETCH_RESTAURANTLOCATION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_RESTAURANTLOCATION):
    case SUCCESS(ACTION_TYPES.UPDATE_RESTAURANTLOCATION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_RESTAURANTLOCATION):
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

const apiUrl = 'api/restaurant-locations';

// Actions

export const getEntities: ICrudGetAllAction<IRestaurantLocation> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_RESTAURANTLOCATION_LIST,
    payload: axios.get<IRestaurantLocation>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IRestaurantLocation> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_RESTAURANTLOCATION,
    payload: axios.get<IRestaurantLocation>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IRestaurantLocation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_RESTAURANTLOCATION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const updateEntity: ICrudPutAction<IRestaurantLocation> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_RESTAURANTLOCATION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRestaurantLocation> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_RESTAURANTLOCATION,
    payload: axios.delete(requestUrl)
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
