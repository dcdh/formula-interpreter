/**
 * Generated by orval v6.24.0 🍺
 * Do not edit manually.
 * infrastructure API
 * OpenAPI spec version: 1.0-SNAPSHOT
 */
import type {
  Execute,
  ExecutionResult,
  SuggestCompletionBody,
  Validate200,
  ValidateBody,
} from "./model";
import { customInstance } from "./mutator/custom-instance";

export const execute = (execute: Execute) => {
  return customInstance<ExecutionResult>({
    url: `/evaluate`,
    method: "POST",
    headers: { "Content-Type": "application/vnd.formula-execute-v1+json" },
    data: execute,
  });
};

export const suggestCompletion = (
  suggestCompletionBody: SuggestCompletionBody,
) => {
  const formData = new FormData();
  formData.append("suggestedFormula", suggestCompletionBody.suggestedFormula);

  return customInstance<string[]>({
    url: `/suggestCompletion`,
    method: "POST",
    headers: { "Content-Type": "multipart/form-data" },
    data: formData,
  });
};

export const validate = (validateBody: ValidateBody) => {
  const formData = new FormData();
  formData.append("formula", validateBody.formula);

  return customInstance<Validate200>({
    url: `/validate`,
    method: "POST",
    headers: { "Content-Type": "multipart/form-data" },
    data: formData,
  });
};

export type ExecuteResult = NonNullable<Awaited<ReturnType<typeof execute>>>;
export type SuggestCompletionResult = NonNullable<
  Awaited<ReturnType<typeof suggestCompletion>>
>;
export type ValidateResult = NonNullable<Awaited<ReturnType<typeof validate>>>;
