//
//  main.m
//  TestGCD
//
//  Created by Mauricio Leal on 1/19/15.
//
//

#import <Foundation/Foundation.h>

int main(int argc, const char * argv[]) {
    @autoreleasepool {
        dispatch_queue_t myConcurrentDispatchQueue =
        dispatch_queue_create("com.example.gcd.MyConcurrentDispatchQueue", nil);
        dispatch_async(myConcurrentDispatchQueue, ^{
            NSLog(@"block on myConcurrentDispatchQueue");
        });
//        dispatch_release(myConcurrentDispatchQueue);
        
        // insert code here...
        NSLog(@"Normal NSLog");
    }
    return 0;
}
