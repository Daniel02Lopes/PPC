import numpy as np
import pandas as pd
import pycuda.driver as cuda
import pycuda.autoinit
from pycuda.compiler import SourceModule

# Read CSV file and functions
df = pd.read_csv("data.csv")
functions = [line.strip() for line in open("functions.txt").readlines()]

# Replace math functions and data columns in the expression
def process_line(line):
    for u in ["sinf", "cosf", "tanf", "sqrtf", "expf"]:
        line = line.replace(u, f"np.{u[:-1]}")
    for c in df.columns:
        line = line.replace(f"_{c}_", f"(df['{c}'].values)")
    return line

# Generate CUDA kernel code dynamically from functions.txt
def generate_kernel_code():
    kernel_code = """
    __device__ float evaluate_expression(float *data, int length, const char* expression) {
        // Evaluate expression using data values
        // Code for evaluating the expression goes here...
    }
    """
    # Generate code for each function and append it to kernel_code

    # Example code to evaluate the provided functions
    for i, function in enumerate(functions):
        function_code = f"""
        if (blockIdx.x == {i}) {{
            // Evaluate function {i}
            // You'll need to implement how to parse and evaluate the function
            // Use process_line() to process the function before evaluation
            float result = evaluate_expression(data, length, "{process_line(function)}");
            results[blockIdx.x] = result;  // Store result
        }}
        """
        kernel_code += function_code

    return kernel_code

# Compile CUDA kernel
module = SourceModule(generate_kernel_code())
evaluate_function = module.get_function("evaluate_expression")

# Prepare data for GPU
data = np.array(df.drop(columns='y'), dtype=np.float32)
target = np.array(df['y'], dtype=np.float32)

# Allocate GPU memory
data_gpu = cuda.mem_alloc(data.nbytes)
target_gpu = cuda.mem_alloc(target.nbytes)

# Transfer data to GPU
cuda.memcpy_htod(data_gpu, data)
cuda.memcpy_htod(target_gpu, target)

# Define grid and block dimensions
block_dim = (1, 1, 1)  # Adjust as needed
grid_dim = (len(functions), 1)

# Launch kernel
evaluate_function(data_gpu, len(df), np.int32(len(df.columns)), target_gpu, block=block_dim, grid=grid_dim)

# Copy results back from GPU
results = np.empty(len(functions), dtype=np.float32)
cuda.memcpy_dtoh(results, results_gpu)

# Find minimum Mean Square Error
best_index = np.argmin(results)
best_score = results[best_index]
best_function = functions[best_index]

print(f"Best score: {best_score} | Best function: {best_function}")