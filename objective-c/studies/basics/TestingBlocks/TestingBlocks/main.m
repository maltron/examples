
#import <Foundation/Foundation.h>

static int multipler = 7;

int main(int argc, const char * argv[]) {
    @autoreleasepool {
        
        float (^oneFrom)(float);
        
        oneFrom = ^(float value) {
            return value*2;
        };
        
        
        NSLog(@"Value is %f", oneFrom(2));
        
    }
    return 0;
}
