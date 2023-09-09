// tslint:disable
/**
 * formula-interpreter API
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 1.0-SNAPSHOT
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

import type {
    Input,
    Range,
} from './';

/**
 * @export
 * @interface ElementExecution
 */
export interface ElementExecution {
    /**
     * @type {string}
     * @memberof ElementExecution
     */
    executedAtStart: string;
    /**
     * @type {string}
     * @memberof ElementExecution
     */
    executedAtEnd: string;
    /**
     * @type {number}
     * @memberof ElementExecution
     */
    processedInNanos: number;
    /**
     * @type {Range}
     * @memberof ElementExecution
     */
    range: Range;
    /**
     * @type {Array<Input>}
     * @memberof ElementExecution
     */
    inputs: Array<Input>;
    /**
     * @type {string}
     * @memberof ElementExecution
     */
    result: string;
}