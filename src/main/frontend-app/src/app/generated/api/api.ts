export * from './executorEndpoint.service';
import { ExecutorEndpointService } from './executorEndpoint.service';
export * from './suggestCompletionEndpoint.service';
import { SuggestCompletionEndpointService } from './suggestCompletionEndpoint.service';
export * from './validatorEndpoint.service';
import { ValidatorEndpointService } from './validatorEndpoint.service';
export const APIS = [ExecutorEndpointService, SuggestCompletionEndpointService, ValidatorEndpointService];
