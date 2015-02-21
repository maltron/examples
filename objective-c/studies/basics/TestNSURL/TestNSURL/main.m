//
//  main.m
//  TestNSURL
//
//  Created by Mauricio Leal on 2/14/15.
//  Copyright (c) 2015 Mauricio "Maltron" Leal. All rights reserved.
//

#import <Foundation/Foundation.h>

int main(int argc, const char * argv[]) {
    @autoreleasepool {
        NSString *example = @"Mauricio Alves Leal";
        
        // insert code here...
        NSLog(@"%@", [example stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]);
    }
    return 0;
}
