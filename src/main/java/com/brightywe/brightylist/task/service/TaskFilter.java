/****************************************************************************
 * Copyright 2020 Jakub Koczur
 *
 * Unauthorized copying of this project, via any medium is strictly prohibited.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES  
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,  
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 *****************************************************************************/

package com.brightywe.brightylist.task.service;

public enum TaskFilter {
    DEFAULT("default", "default"),
    BY_PRIORITY_HIGH("priority", "1"),
    BY_PRIORITY_MEDIUM("priority", "2"),
    BY_PRIORITY_LOW("priority", "3"),
    STATUS_PENDING("status", "STATUS_PENDING"),
    STATUS_ACTIVE("status", "STATUS_ACTIVE"),
    STATUS_COMPLETED("status", "STATUS_COMPLETED"),
    STATUS_OVERDUE("status", "STATUS_OVERDUE");
    
    public final String key;
    public final String data;
    
    private TaskFilter(String key, String data) {
        this.key = key;
        this.data = data;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public String getData() {
        return this.data;
    }
}
