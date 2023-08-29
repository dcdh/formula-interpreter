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

import type { Observable } from 'rxjs';
import type { AjaxResponse } from 'rxjs/ajax';
import { BaseAPI, throwIfNullOrUndefined } from '../runtime';
import type { OperationOpts, HttpHeaders } from '../runtime';
import type {
    Execute400Response,
    ExecuteDTO,
    ExecutionResultDTO,
} from '../models';

export interface ExecuteRequest {
    executeDTO: ExecuteDTO;
}

/**
 * no description
 */
export class ExecutorEndpointApi extends BaseAPI {

    /**
     */
    execute({ executeDTO }: ExecuteRequest): Observable<ExecutionResultDTO>
    execute({ executeDTO }: ExecuteRequest, opts?: OperationOpts): Observable<AjaxResponse<ExecutionResultDTO>>
    execute({ executeDTO }: ExecuteRequest, opts?: OperationOpts): Observable<ExecutionResultDTO | AjaxResponse<ExecutionResultDTO>> {
        throwIfNullOrUndefined(executeDTO, 'executeDTO', 'execute');

        const headers: HttpHeaders = {
            'Content-Type': 'application/vnd.formula-execute-v1+json',
        };

        return this.request<ExecutionResultDTO>({
            url: '/execute',
            method: 'POST',
            headers,
            body: executeDTO,
        }, opts?.responseOpts);
    };

}
