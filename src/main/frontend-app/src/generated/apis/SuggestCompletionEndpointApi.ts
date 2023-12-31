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
import type { OperationOpts } from '../runtime';
import type {
    ErrorMessage,
} from '../models';

export interface SuggestCompletionRequest {
    suggestedFormula: string;
}

/**
 * no description
 */
export class SuggestCompletionEndpointApi extends BaseAPI {

    /**
     */
    suggestCompletion({ suggestedFormula }: SuggestCompletionRequest): Observable<Array<string>>
    suggestCompletion({ suggestedFormula }: SuggestCompletionRequest, opts?: OperationOpts): Observable<AjaxResponse<Array<string>>>
    suggestCompletion({ suggestedFormula }: SuggestCompletionRequest, opts?: OperationOpts): Observable<Array<string> | AjaxResponse<Array<string>>> {
        throwIfNullOrUndefined(suggestedFormula, 'suggestedFormula', 'suggestCompletion');

        const formData = new FormData();
        if (suggestedFormula !== undefined) { formData.append('suggestedFormula', suggestedFormula as any); }

        return this.request<Array<string>>({
            url: '/suggestCompletion',
            method: 'POST',
            body: formData,
        }, opts?.responseOpts);
    };

}
