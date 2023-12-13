import numpy as np
import pandas as pd
import pycuda.driver as cuda
import pycuda.autoinit
from pycuda.compiler import SourceModule


df = pd.read_csv("data.csv")
functions = [line.strip() for line in open("functions.txt").readlines()]
num_functions = len(functions)
num_rows = len(df)

def process_line(line):
    for u in ["sinf", "cosf", "tanf", "sqrtf", "expf"]:
        line = line.replace(u, f"np.{u[:-1]}")
    for c in df.columns:
        line = line.replace(f"_{c}_", f"(df['{c}'].values)")
    return line

kernel_code = f"""
__global__ void evaluate_functions(float *output, float *input_data, int num_rows, int num_functions) {{
    int idx = threadIdx.x + blockIdx.x * blockDim.x;

    if (idx < num_functions) {{
        final int id = idx;
        float result = 0.0f;
        for (int i = 0; i < num_rows; ++i) {{
            float val = {process_line(functions[id])};
            result += val;
        }}
        output[id] = result / num_rows;  // Calculate mean for each function
    }}
}}
"""


mod = SourceModule(kernel_code)
evaluate_functions = mod.get_function("evaluate_functions")

input_data = np.zeros((num_rows, len(df.columns) - 1), dtype=np.float32)
output_data = np.zeros(num_functions, dtype=np.float32)


for i, column in enumerate(df.columns[:-1]): 
    input_data[:, i] = df[column].values.astype(np.float32)

input_data_gpu = cuda.mem_alloc(input_data.nbytes)
output_data_gpu = cuda.mem_alloc(output_data.nbytes)

cuda.memcpy_htod(input_data_gpu, input_data)
cuda.memcpy_htod(output_data_gpu, output_data)

block_size = 256
grid_size = (num_functions + block_size - 1) // block_size

evaluate_functions(output_data_gpu, input_data_gpu, np.int32(num_rows), np.int32(num_functions),
                   block=(block_size, 1, 1), grid=(grid_size, 1))

cuda.memcpy_dtoh(output_data, output_data_gpu)

mse_values = np.square(output_data - df['y'].values).mean(axis=0)

best_score_index = np.argmin(mse_values)
best_score = mse_values[best_score_index]
best_function = functions[best_score_index]

print(f"Best score: {best_score} | Best function: {best_function}")