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
    ElementExecutionDTO,
} from './';

/**
 * @export
 * @interface ExecutionResultDTO
 */
export interface ExecutionResultDTO {
    /**
     * @type {string}
     * @memberof ExecutionResultDTO
     */
    executedAtStart?: string;
    /**
     * @type {string}
     * @memberof ExecutionResultDTO
     */
    executedAtEnd?: string;
    /**
     * @type {number}
     * @memberof ExecutionResultDTO
     */
    processedInNanos?: number;
    /**
     * @type {string}
     * @memberof ExecutionResultDTO
     */
    result?: string;
    /**
     * @type {Array<ElementExecutionDTO>}
     * @memberof ExecutionResultDTO
     */
    elementExecutions?: Array<ElementExecutionDTO>;
}
