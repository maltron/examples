//
//  main.m
//  TestBlock
//
//  Created by Mauricio Leal on 2/5/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef NSString * (^IntToStringConverter)(NSInteger parameter);

@interface Example : NSObject
-(NSString *)convertIntToString:(NSUInteger)paramInteger usingBlockObject:(IntToStringConverter)paramBlockObject;

@end


@implementation Example

-(NSString *)convertIntToString:(NSUInteger)paramInteger usingBlockObject:(IntToStringConverter)paramBlockObject {
    return paramBlockObject(paramInteger);
}

@end

int main(int argc, const char * argv[]) {
    @autoreleasepool {
        Example *myExample = [[Example alloc] init];
        
        IntToStringConverter inlineConverter = ^(NSInteger parameter) {
            return [NSString stringWithFormat:@"%lu", (unsigned long)parameter];
        };
        
        NSString *result = [myExample convertIntToString:123 usingBlockObject:inlineConverter];
        NSLog(@"The Result of 123 is %@", result);
    }
    return 0;
}
