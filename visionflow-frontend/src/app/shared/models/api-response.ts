// generic response wrapper
export interface ApiResponse<T> {
    success: boolean;
    timestamp: string;
    message: string;
    path: string;
    data: T;
}
